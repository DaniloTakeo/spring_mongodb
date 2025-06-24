package com.bustation.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Document(collection = "motoristas")
@Data
@AllArgsConstructor
public class Motorista {

    @Id
    private String id;

    private String nome;
    private String cnh;
    private String categoria;

}