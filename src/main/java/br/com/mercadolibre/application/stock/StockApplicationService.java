package br.com.mercadolibre.application.stock;

import br.com.mercadolibre.api.model.PurchaseRequest;
import br.com.mercadolibre.api.model.PurchaseResponse;
import br.com.mercadolibre.api.model.StockResponse;
import br.com.mercadolibre.application.redis.RedisCacheService;
import br.com.mercadolibre.infra.message.InventoryUpdatePublisher;
import br.com.mercadolibre.infra.message.model.EventPayload;
import br.com.mercadolibre.infra.message.model.Payload;
import br.com.mercadolibre.infra.sql.stock.model.StockEntity;
import br.com.mercadolibre.infra.sql.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static br.com.mercadolibre.core.constants.CacheNameConstant.PRODUCTS_BY_CATEGORY;
import static br.com.mercadolibre.infra.message.model.ChangeType.DECREASE;
import static br.com.mercadolibre.infra.message.model.EventType.UPDATED;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class StockApplicationService {

    private final StockRepository stockRepository;
    private final RedisCacheService redisCacheService;
    private final InventoryUpdatePublisher inventoryUpdateProducer;

    public List<StockResponse> getStocks() {
        var stocks = stockRepository.findAll();
        return stocks.stream()
                .map(this::toStockResponse)
                .toList();
    }
    
    @Transactional
    public PurchaseResponse update(PurchaseRequest request) {
        var productId = request.getProductId();
        var storeId = request.getStoreId();

        var idempotencyKey = request.getCustomerId() + "-" + productId + "-" + storeId + "-" + request.getQuantity();
        var isFirst = redisCacheService.setIfAbsent(idempotencyKey, "processing");

        if (!isFirst) {
            throw new IllegalArgumentException("Requisição duplicada detectada. Por favor, tente novamente.");
        }

        var stock = stockRepository.findByProductIdAndStoreIdWithLock(productId, storeId)
                .orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado"));

        if (stock.getAvailableQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException(
                format("Estoque insuficiente. Disponível: %d, Solicitado: %d",
                             stock.getAvailableQuantity(), request.getQuantity())
            );
        }
        
        stock.setQuantity(stock.getQuantity() - request.getQuantity());
        stock.setAvailableQuantity(stock.getAvailableQuantity() - request.getQuantity());
        
        var updatedStock = stockRepository.save(stock);
        redisCacheService.evict(PRODUCTS_BY_CATEGORY, updatedStock.getProduct().getCategory());

        var orderId = "ORDER-" + UUID.randomUUID();

        var payload = Payload.builder()
                .productId(updatedStock.getProduct().getId().toString())
                .storeId(updatedStock.getStore().getId().toString())
                .quantity(updatedStock.getQuantity())
                .availableQuantity(updatedStock.getAvailableQuantity())
                .reservedQuantity(updatedStock.getReservedQuantity())
                .build();

        var eventId = updatedStock.getProduct().getSku() + "-" + updatedStock.getStore().getStoreCode() + "-" + updatedStock.getUpdatedAt().toString();
        var messageEvent = EventPayload.builder()
                .eventId(eventId)
                .eventType(UPDATED)
                .changeType(DECREASE)
                .aggregateId(updatedStock.getProduct().getSku() + "-" + updatedStock.getStore().getStoreCode())
                .source("stock")
                .createdAt(updatedStock.getUpdatedAt())
                .payload(payload)
                .build();



        inventoryUpdateProducer.sendMessageAsync(messageEvent);

        return PurchaseResponse.builder()
                .success(true)
                .message("Compra realizada com sucesso")
                .orderId(orderId)
                .productName(stock.getProduct().getName())
                .storeName(stock.getStore().getName())
                .quantityPurchased(request.getQuantity())
                .remainingStock(updatedStock.getAvailableQuantity())
                .build();
    }

    private StockResponse toStockResponse(StockEntity entity) {
        return StockResponse.builder()
                .id(entity.getId().toString())
                .productId(entity.getProduct().getId().toString())
                .productName(entity.getProduct().getName())
                .productSku(entity.getProduct().getSku())
                .storeId(entity.getStore().getId().toString())
                .storeName(entity.getStore().getName())
                .storeCode(entity.getStore().getStoreCode())
                .quantity(entity.getQuantity())
                .reservedQuantity(entity.getReservedQuantity())
                .availableQuantity(entity.getAvailableQuantity())
                .build();
    }
}
