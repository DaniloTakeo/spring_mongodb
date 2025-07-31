package com.bustation.mongodb.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Classe de configuração da documentação da API utilizando OpenAPI (Swagger).
 *
 * <p>Define as informações básicas da API expostas na interface Swagger UI,
 * como título, descrição e versão.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Cria e configura a instância do objeto {@link OpenAPI} que será usada
     * pelo Swagger para gerar a documentação da API.
     *
     * @return uma instância configurada de {@link OpenAPI}
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Rodoviária")
                        .description("Documentação da API REST de"
                                + " rodoviária com Spring Boot + MongoDB +"
                                + " JWT + Redis")
                        .version("1.0.0")
                );
    }
}
