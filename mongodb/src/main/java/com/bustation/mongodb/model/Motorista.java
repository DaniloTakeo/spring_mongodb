package com.bustation.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa um motorista cadastrado no sistema.
 *
 * Cada motorista possui um nome, CNH e categoria correspondente.
 */
@Document(collection = "motoristas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Motorista {

    /**
     * Identificador único do motorista.
     */
    @Id
    private String id;

    /**
     * Nome completo do motorista.
     */
    private String nome;

    /**
     * Número da CNH do motorista.
     */
    private String cnh;

    /**
     * Categoria da CNH do motorista.
     */
    private String categoria;
}
