package com.bustation.mongodb.config.cache;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuração de cache para a aplicação, utilizando Redis como provedor.
 * Define o tempo de vida padrão dos itens no cache e a serialização.
 */
@Configuration
@Slf4j
public class CacheConfig {

    /**
     * Define o tempo de vida padrão (TTL) para os itens do cache em minutos.
     */
    private static final int CACHE_TTL_MINUTES = 10;

    /**
     * Configura o comportamento padrão do cache Redis.
     * Define o tempo de vida dos itens e o serializador
     * para os valores em JSON.
     *
     * @return A configuração padrão do cache Redis.
     */
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        log.info("Configurando RedisCache com TTL de {} minutos",
                CACHE_TTL_MINUTES);
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(CACHE_TTL_MINUTES))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(
                                        new
                                        GenericJackson2JsonRedisSerializer()));
    }

    /**
     * Configura o ObjectMapper para
     * serialização e desserialização de objetos JSON,
     * incluindo suporte para tipos de data e hora do Java 8 (java.time).
     *
     * @return Um ObjectMapper configurado.
     */
    @Bean
    public ObjectMapper objectMapper() {
        log.debug("Configurando ObjectMapper com suporte a JavaTimeModule");
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
