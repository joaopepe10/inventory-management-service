package br.com.mercadolibre.application.store.model;

import lombok.Builder;

@Builder
public record StoreDTO(
        String id,
        String storeCode,
        String name
) {
}
