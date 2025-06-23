package com.bustation.mongodb.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Rodoviária")
                        .description("Documentação da API REST de rodoviária com Spring Boot + MongoDB + JWT + Redis")
                        .version("1.0.0")
                );
    }
}