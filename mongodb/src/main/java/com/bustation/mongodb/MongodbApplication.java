package com.bustation.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import lombok.extern.slf4j.Slf4j;

/**
 * Classe principal da aplicação Spring Boot que inicializa o contexto
 * da aplicação e habilita o mecanismo de cache.
 */
@SpringBootApplication
@EnableCaching
@Slf4j
public final class MongodbApplication {

    /**
     * Método principal que inicia a aplicação.
     *
     * @param args argumentos de linha de comando
     */
    public static void main(final String[] args) {
        log.info("Iniciando aplicação MongodbApplication...");
        SpringApplication.run(MongodbApplication.class, args);
        log.info("Aplicação MongodbApplication iniciada com sucesso.");
    }

    /**
     * Construtor padrão privado para evitar instanciação da classe.
     */
    private MongodbApplication() {
        // Evita instanciação
    }
}
