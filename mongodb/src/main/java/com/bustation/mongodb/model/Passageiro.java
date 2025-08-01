package com.bustation.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa um passageiro do sistema.
 *
 * Contém dados como nome, CPF e e-mail.
 */
@Document(collection = "passageiros")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passageiro {

    /**
     * Identificador único do passageiro.
     */
    @Id
    private String id;

    /**
     * Nome completo do passageiro.
     */
    private String nome;

    /**
     * CPF do passageiro.
     */
    private String cpf;

    /**
     * Endereço de e-mail do passageiro.
     */
    private String email;
}
