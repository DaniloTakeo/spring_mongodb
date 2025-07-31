/**
 * Este pacote contém classes de configuração relacionadas ao sistema
 * de cache da aplicação.
 *
 * <p>Atualmente, utiliza Redis como provedor de cache,
 * com configurações personalizadas
 * de serialização (em JSON) e tempo de expiração dos dados armazenados (TTL).
 * A configuração garante suporte adequado a
 * tipos modernos como os de data/hora do Java 8
 * por meio de um
 * {@link com.fasterxml.jackson.databind.ObjectMapper} customizado.
 *
 * <p>As configurações aqui presentes são aplicadas automaticamente pelo Spring
 * por meio da anotação
 * {@link org.springframework.context.annotation.Configuration}.
 *
 * @see com.bustation.mongodb.config.cache.CacheConfig
 */

package com.bustation.mongodb.config.cache;
