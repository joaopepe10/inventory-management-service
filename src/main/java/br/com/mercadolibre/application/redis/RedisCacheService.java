package br.com.mercadolibre.application.redis;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static br.com.mercadolibre.core.configuration.cache.CacheConfigTTL.IDEMPOTENCY_KEY;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private final CacheManager cacheManager;
    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void clearAllCachesOnStartup() {
        cacheManager.getCacheNames().forEach(name -> {
            var cache = cacheManager.getCache(name);
            if (nonNull(cache)) {
                cache.clear();
            }
        });
    }

    public boolean setIfAbsent(String key, String value ) {
        var duration = Duration.ofSeconds(IDEMPOTENCY_KEY.getTtl());
        var result = redisTemplate.opsForValue().setIfAbsent(key, value, duration);
        return TRUE.equals(result);
    }

    public void evict(String cacheName, Object key) {
        var cache = cacheManager.getCache(cacheName);
        if (nonNull(cache)) {
            cache.evictIfPresent(key);
        }
    }
}
