package com.bustation.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "onibus")
@Data
public class Onibus {

    @Id
    private String id;

    private String placa;
    private String modelo;
    private int capacidade;

}