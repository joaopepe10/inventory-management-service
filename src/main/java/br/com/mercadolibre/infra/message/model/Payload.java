package br.com.mercadolibre.infra.message.model;

import lombok.Builder;

@Builder
public record Payload(
        String productId,
        String storeId,
        Integer quantity,
        Integer availableQuantity,
        Integer reservedQuantity
) {
}
