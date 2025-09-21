package br.com.mercadolibre.domain.stock.mapper;

import br.com.mercadolibre.api.model.PurchaseResponse;
import br.com.mercadolibre.api.model.StockResponse;
import br.com.mercadolibre.application.stock.model.StockDTO;
import br.com.mercadolibre.infra.sql.stock.model.StockEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockMapper {

    List<StockResponse> toResponse(List<StockEntity> stockEntities);

    @Mapping(target = "productId", source = "stockEntity.product.id")
    @Mapping(target = "productName", source = "stockEntity.product.name")
    @Mapping(target = "productSku", source = "stockEntity.product.sku")
    @Mapping(target = "storeId", source = "stockEntity.store.id")
    @Mapping(target = "storeName", source = "stockEntity.store.name")
    @Mapping(target = "storeCode", source = "stockEntity.store.storeCode")
    @Mapping(target = "isLowStock", expression = "java(stockEntity.getQuantity() <= 5)")
    StockResponse toResponse(StockEntity stockEntity);

    @Mapping(target = "remainingStock", expression = "java(updatedEntity.getQuantity())")
    StockDTO toDto(StockEntity updatedEntity, Integer quantityPurchased);

    @Mapping(target = "success", constant = "true")
    @Mapping(target = "message", constant = "Compra realizada com sucesso")
    @Mapping(target = "productName", source = "dto.product.name")
    @Mapping(target = "storeName", source = "dto.store.name")
    PurchaseResponse toPurchaseResponse(StockDTO dto, String orderId, Integer quantityPurchased);
}
