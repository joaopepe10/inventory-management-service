package br.com.mercadolibre.application.product.model;

import lombok.Builder;

@Builder
public record ProductDTO(
        String id,
        String name,
        String category,
        String sku
) {


}
