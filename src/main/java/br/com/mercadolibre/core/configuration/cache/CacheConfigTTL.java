package br.com.mercadolibre.core.configuration.cache;

import lombok.Getter;

@Getter
public enum CacheConfigTTL {

    PRODUCTS_BY_CATEGORY(10L),
    IDEMPOTENCY_KEY(5L);

    private final long ttl;

    CacheConfigTTL(Long ttl) {
        this.ttl = ttl;
    }
}
