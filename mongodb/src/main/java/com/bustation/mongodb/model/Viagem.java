package com.bustation.mongodb.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * Representa uma viagem planejada pela empresa de transporte.
 *
 * Contém informações como origem, destino, data, ônibus e motorista.
 */
@Data
@Document(collection = "viagens")
public class Viagem {

    /**
     * Identificador único da viagem.
     */
    @Id
    private String id;

    /**
     * Cidade de origem da viagem.
     */
    private String origem;

    /**
     * Cidade de destino da viagem.
     */
    private String destino;

    /**
     * Data e hora de saída da viagem.
     */
    private LocalDateTime dataHora;

    /**
     * ID do ônibus associado à viagem.
     */
    private String idOnibus;

    /**
     * ID do motorista associado à viagem.
     */
    private String idMotorista;
}
