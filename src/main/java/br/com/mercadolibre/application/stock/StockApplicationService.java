package br.com.mercadolibre.application.stock;

import br.com.mercadolibre.api.model.PurchaseRequest;
import br.com.mercadolibre.api.model.PurchaseResponse;
import br.com.mercadolibre.api.model.StockResponse;
import br.com.mercadolibre.application.stock.model.StockDTO;
import br.com.mercadolibre.domain.stock.StockService;
import br.com.mercadolibre.domain.stock.mapper.StockMapper;
import br.com.mercadolibre.infra.message.InventoryUpdatePublisher;
import br.com.mercadolibre.infra.message.model.Payload;
import br.com.mercadolibre.infra.message.model.UpdateInventoryMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static br.com.mercadolibre.core.configuration.cache.CacheConfigTTL.IDEMPOTENCY_KEY;
import static br.com.mercadolibre.core.constants.CacheNameConstant.PRODUCTS_BY_CATEGORY;
import static br.com.mercadolibre.infra.message.model.ChangeType.DECREASE;
import static br.com.mercadolibre.infra.message.model.EventType.UPDATED;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class StockApplicationService {

    private final InventoryUpdatePublisher inventoryUpdateProducer;
    private final StockService stockService;
    private final StockMapper stockMapper;
    private final CacheManager cacheManager;
    private final RedisTemplate<String, String> redisTemplate;

    public List<StockResponse> getStocks() {
        return stockService.findAll();
    }

    public StockResponse findById(UUID id) {
        return stockService.findById(id);
    }
    
    public PurchaseResponse purchase(PurchaseRequest request) {
        var productId = request.getProductId();
        var storeId = request.getStoreId();

        var idempotencyKey = request.getCustomerId() + "-" + productId + "-" + storeId + "-" + request.getQuantity();
        var isFirst = setIfAbsent(idempotencyKey, "processing");

        if (!isFirst) {
            throw new IllegalArgumentException("Requisição duplicada detectada. Por favor, tente novamente.");
        }

        var updatedStockDto = stockService.update(request);

        evictCache(updatedStockDto);

        var orderId = "ORDER-" + UUID.randomUUID();

        sendEvent(updatedStockDto);

        return stockMapper.toPurchaseResponse(updatedStockDto, orderId, request.getQuantity());
    }

    private void evictCache(StockDTO updatedStockDto) {
        evict(updatedStockDto.product().category());
    }

    private void evict(Object key) {
        var cache = cacheManager.getCache(PRODUCTS_BY_CATEGORY);
        if (nonNull(cache)) {
            cache.evictIfPresent(key);
        }
    }

    private void sendEvent(StockDTO updatedStockDto) {
        var eventId = makeEventId(updatedStockDto);

        var messageEvent = makeMessage(eventId, updatedStockDto);

        inventoryUpdateProducer.sendMessageAsync(messageEvent);
    }

    private boolean setIfAbsent(String key, String value ) {
        var duration = Duration.ofSeconds(IDEMPOTENCY_KEY.getTtl());
        var result = redisTemplate.opsForValue().setIfAbsent(key, value, duration);
        return TRUE.equals(result);
    }


    private static Payload makePayload(StockDTO updatedStock) {
        return Payload.builder()
                .productId(updatedStock.product().id())
                .storeId(updatedStock.store().id())
                .quantity(updatedStock.quantity())
                .build();
    }

    private static UpdateInventoryMessage makeMessage(String eventId, StockDTO updatedStock) {
        var payload = makePayload(updatedStock);
        return UpdateInventoryMessage.builder()
                .eventId(eventId)
                .eventType(UPDATED)
                .changeType(DECREASE)
                .aggregateId(updatedStock.product().sku() + "-" + updatedStock.store().storeCode())
                .source("stock")
                .createdAt(updatedStock.updatedAt())
                .payload(payload)
                .build();
    }

    private static String makeEventId(StockDTO updatedStock) {
        return updatedStock.product().sku() + "-" + updatedStock.store().storeCode() + "-" + updatedStock.updatedAt().toString();
    }

}
