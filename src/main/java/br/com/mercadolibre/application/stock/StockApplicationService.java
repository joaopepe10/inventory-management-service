package br.com.mercadolibre.application.stock;

import br.com.mercadolibre.api.model.PurchaseRequest;
import br.com.mercadolibre.api.model.PurchaseResponse;
import br.com.mercadolibre.api.model.StockResponse;
import br.com.mercadolibre.application.redis.RedisCacheService;
import br.com.mercadolibre.application.stock.model.StockDTO;
import br.com.mercadolibre.domain.stock.StockService;
import br.com.mercadolibre.domain.stock.mapper.StockMapper;
import br.com.mercadolibre.infra.message.InventoryUpdatePublisher;
import br.com.mercadolibre.infra.message.model.Payload;
import br.com.mercadolibre.infra.message.model.UpdateInventoryMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static br.com.mercadolibre.core.constants.CacheNameConstant.PRODUCTS_BY_CATEGORY;
import static br.com.mercadolibre.infra.message.model.ChangeType.DECREASE;
import static br.com.mercadolibre.infra.message.model.EventType.UPDATED;

@Service
@RequiredArgsConstructor
public class StockApplicationService {

    private final RedisCacheService redisCacheService;
    private final InventoryUpdatePublisher inventoryUpdateProducer;
    private final StockService stockService;
    private final StockMapper stockMapper;

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
        var isFirst = redisCacheService.setIfAbsent(idempotencyKey, "processing");

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
        redisCacheService.evict(PRODUCTS_BY_CATEGORY, updatedStockDto.product().category());
    }

    private void sendEvent(StockDTO updatedStockDto) {
        var eventId = makeEventId(updatedStockDto);

        var messageEvent = makeMessage(eventId, updatedStockDto);

        inventoryUpdateProducer.sendMessageAsync(messageEvent);
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
