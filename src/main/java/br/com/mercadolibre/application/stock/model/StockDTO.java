package br.com.mercadolibre.application.stock.model;

import br.com.mercadolibre.application.product.model.ProductDTO;
import br.com.mercadolibre.application.store.model.StoreDTO;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record StockDTO(
        ProductDTO product,
        StoreDTO store,
        Integer reservedQuantity,
        Integer availableQuantity,
        Integer quantity,
        LocalDateTime updatedAt
        ) {
}
