package com.bustation.mongodb.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * Representa uma reserva realizada por um passageiro para uma viagem.
 */
@Document(collection = "reservas")
@Data
public class Reserva {

    /**
     * Identificador único da reserva.
     */
    @Id
    private String id;

    /**
     * ID do passageiro que fez a reserva.
     */
    private String idPassageiro;

    /**
     * ID da viagem reservada.
     */
    private String idViagem;

    /**
     * Data e hora em que a reserva foi realizada.
     */
    private LocalDateTime dataReserva;

    /**
     * Número do assento reservado.
     */
    private int assento;
}
