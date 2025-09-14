package br.com.mercadolibre.infra.message.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record Payload(
        @NotBlank
        String productId,

        @NotBlank
        String storeId,

        @NotNull
        Integer quantity,

        @NotNull
        Integer availableQuantity,

        @NotNull
        Integer reservedQuantity
) {
}
