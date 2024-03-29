package com.example.web_2.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer(ObjectMapper objectMapper) {
        ObjectMapper copy = objectMapper.copy();
        copy.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        copy.activateDefaultTyping(
                copy.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.EVERYTHING,
                JsonTypeInfo.As.PROPERTY);
        return new GenericJackson2JsonRedisSerializer(copy);
    }

    @Bean
    public RedisCacheManager cacheManager(ObjectMapper objectMapper) {
        RedisCacheConfiguration myCfg = myDefaultCacheConfig(objectMapper);
        RedisCacheConfiguration cacheConfig = myCfg.disableCachingNullValues();

        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(cacheConfig)
                .withCacheConfiguration("brands", myCfg)
                .withCacheConfiguration("brandPage", myCfg)
                .withCacheConfiguration("models", myCfg)
                .withCacheConfiguration("modelPage", myCfg)
                .withCacheConfiguration("brandModels", myCfg)
                .withCacheConfiguration("offers", myCfg)
                .withCacheConfiguration("offerPage", myCfg)
                .withCacheConfiguration("userOffers", myCfg)
                .withCacheConfiguration("users", myCfg)
                .withCacheConfiguration("userPage", myCfg)
                .withCacheConfiguration("profile", myCfg)
                .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                       GenericJackson2JsonRedisSerializer serializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setDefaultSerializer(serializer);
        return template;
    }



    private RedisCacheConfiguration myDefaultCacheConfig(ObjectMapper objectMapper) {
        GenericJackson2JsonRedisSerializer serializer = genericJackson2JsonRedisSerializer(objectMapper);
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(30))
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(serializer));
    }
}