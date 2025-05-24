package com.bustation.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "passageiros")
@Data
public class Passageiro {

    @Id
    private String id;

    private String nome;
    private String cpf;
    private String email;

}