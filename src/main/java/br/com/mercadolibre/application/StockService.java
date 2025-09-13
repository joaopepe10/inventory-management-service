package br.com.mercadolibre.application;

import br.com.mercadolibre.infra.sql.stock.model.MovementType;
import br.com.mercadolibre.infra.sql.stock.model.StockEntity;
import br.com.mercadolibre.infra.sql.stock.model.StockMovementEntity;
import br.com.mercadolibre.infra.sql.stock.repository.StockMovementRepository;
import br.com.mercadolibre.infra.sql.stock.repository.StockRepository;
import br.com.mercadolibre.api.model.PurchaseRequest;
import br.com.mercadolibre.api.model.PurchaseResponse;
import br.com.mercadolibre.api.model.StockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static java.lang.String.*;

@Service
@RequiredArgsConstructor
public class StockService {
    
    private final StockRepository stockRepository;
    private final StockMovementRepository stockMovementRepository;
    
    public List<StockResponse> getStocks(String storeId, String productId) {
        var stocks = stockRepository.findAll();
        return stocks.stream()
                .map(this::toStockResponse)
                .toList();
    }
    
    @Transactional
    public PurchaseResponse simulatePurchase(PurchaseRequest request) {
        var productId = UUID.fromString(request.getProductId());
        var storeId = UUID.fromString(request.getStoreId());
        var stock = stockRepository.findByProductIdAndStoreIdWithLock(productId, storeId)
                .orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado"));
        
        if (stock.getAvailableQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException(
                format("Estoque insuficiente. Disponível: %d, Solicitado: %d",
                             stock.getAvailableQuantity(), request.getQuantity())
            );
        }
        
        var previousQuantity = stock.getQuantity();
        stock.setQuantity(stock.getQuantity() - request.getQuantity());
        stock.setAvailableQuantity(stock.getAvailableQuantity() - request.getQuantity());
        
        var updatedStock = stockRepository.save(stock);
        
        var orderId = request.getOrderId() != null ? request.getOrderId() : "ORDER-" + UUID.randomUUID();
        createStockMovement(updatedStock, MovementType.OUTBOUND, request.getQuantity(),
                           previousQuantity, updatedStock.getQuantity(), "Venda", orderId);
        
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

    private void createStockMovement(StockEntity stock, MovementType type, Integer quantity,
                                   Integer previousQuantity, Integer newQuantity, 
                                   String reason, String referenceId) {
        StockMovementEntity movement = StockMovementEntity.builder()
                .productId(stock.getProduct().getId())
                .storeId(stock.getStore().getId())
                .movementType(type)
                .quantity(quantity)
                .previousQuantity(previousQuantity)
                .newQuantity(newQuantity)
                .reason(reason)
                .referenceId(referenceId)
                .build();
        
        stockMovementRepository.save(movement);
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
                .minimumStock(entity.getMinimumStock())
                .build();
    }
}
