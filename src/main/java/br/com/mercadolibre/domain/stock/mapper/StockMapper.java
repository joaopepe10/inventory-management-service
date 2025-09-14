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

    StockResponse toResponse(StockEntity stockEntity);

    StockDTO toDto(StockEntity updatedEntity);

    @Mapping(target = "success", constant = "true")
    @Mapping(target = "message", constant = "Compra realizada com sucesso")
    @Mapping(target = "productName", source = "dto.product.name")
    @Mapping(target = "storeName", source = "dto.store.name")
    @Mapping(target = "remainingStock", source = "dto.availableQuantity")
    PurchaseResponse toPurchaseResponse(StockDTO dto, String orderId, Integer quantityPurchased);
}
