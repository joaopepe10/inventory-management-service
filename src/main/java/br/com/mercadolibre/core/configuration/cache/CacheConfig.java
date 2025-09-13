package br.com.mercadolibre.core.configuration.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static br.com.mercadolibre.core.configuration.cache.CacheConfigTTL.PRODUCTS_BY_CATEGORY;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@Configuration
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        RedisCacheConfiguration serializationConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(fromSerializer(new GenericJackson2JsonRedisSerializer()));

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put(PRODUCTS_BY_CATEGORY.name(), valuesAtMinutes(PRODUCTS_BY_CATEGORY.getTtl()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(serializationConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    private static RedisCacheConfiguration valuesAtMinutes(long minutes) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofMinutes(minutes));
    }
}
