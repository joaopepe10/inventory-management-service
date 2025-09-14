package br.com.mercadolibre.infra.message.model;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
public record UpdateInventoryMessage(
        String productId,
        String sku,
        String storeId,
        String storeCode,
        Integer quantity,
        Integer reservedQuantity,
        Integer availableQuantity,
        LocalDateTime updatedAt
) implements Serializable {
}
