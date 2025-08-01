package com.bustation.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Classe principal da aplicação Spring Boot que inicializa o contexto
 * da aplicação e habilita o mecanismo de cache.
 */
@SpringBootApplication
@EnableCaching
public final class MongodbApplication {

    /**
     * Método principal que inicia a aplicação.
     *
     * @param args argumentos de linha de comando
     */
    public static void main(final String[] args) {
        SpringApplication.run(MongodbApplication.class, args);
    }

    /**
     * Construtor pdrão privado para evitar instanciação da classe.
     */
    private MongodbApplication() { }
}
