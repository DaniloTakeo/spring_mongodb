package com.bustation.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa um ônibus disponível para viagens.
 *
 * Contém informações como placa, modelo e capacidade de assentos.
 */
@Document(collection = "onibus")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Onibus {

    /**
     * Identificador único do ônibus.
     */
    @Id
    private String id;

    /**
     * Placa do ônibus.
     */
    private String placa;

    /**
     * Modelo do ônibus.
     */
    private String modelo;

    /**
     * Capacidade total de passageiros.
     */
    private int capacidade;
}
