package br.com.mercadolibre.application.redis;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private final CacheManager cacheManager;

    @PostConstruct
    public void clearAllCachesOnStartup() {
        cacheManager.getCacheNames().forEach(name -> {
            var cache = cacheManager.getCache(name);
            if (nonNull(cache)) {
                cache.clear();
            }
        });
    }

    public void evict(String cacheName, Object key) {
        var cache = cacheManager.getCache(cacheName);
        if (nonNull(cache)) {
            cache.evictIfPresent(key);
        }
    }
}
