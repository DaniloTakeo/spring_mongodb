package com.bustation.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "passageiros")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passageiro {

    @Id
    private String id;

    private String nome;
    private String cpf;
    private String email;

}