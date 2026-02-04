package com.radiuk.user_management_service.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.radiuk.user_management_service.util.RedisConfigNames;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class RedisConfig {

    @Value("${jwt.refresh-token-ttl}")
    private Duration refreshTokenTtl;

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory, ObjectMapper objectMapper) {
        RedisCacheConfiguration baseConfig = createBaseConfig();

        Function<CacheConfigEntry, RedisCacheConfiguration> createConfig = createConfig(baseConfig);

        Jackson2JsonRedisSerializer<Boolean> booleanSerializer = new Jackson2JsonRedisSerializer<>(Boolean.class);
        booleanSerializer.setObjectMapper(objectMapper);

        Map<String, RedisCacheConfiguration> configs = Map.of(
                RedisConfigNames.VALID_REFRESH_JTI, createConfig.apply(new CacheConfigEntry(refreshTokenTtl, booleanSerializer))
        );

        return RedisCacheManager.builder(factory)
                .cacheDefaults(baseConfig)
                .withInitialCacheConfigurations(configs)
                .transactionAware()
                .build();
    }

    private RedisCacheConfiguration createBaseConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeKeysWith(
                        RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(new StringRedisSerializer())
                );
    }
    private Function<CacheConfigEntry, RedisCacheConfiguration> createConfig(RedisCacheConfiguration baseConfig) {
        return entry -> baseConfig
                .entryTtl(entry.ttl)
                .serializeValuesWith(
                        RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(entry.valueSerializer)
                );
    }

    private record CacheConfigEntry(
            Duration ttl,
            RedisSerializer<?> valueSerializer
    ) {}
}
