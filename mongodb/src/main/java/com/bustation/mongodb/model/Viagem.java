package com.bustation.mongodb.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "viagens")
public class Viagem {

    @Id
    private String id;

    private String origem;
    private String destino;
    private LocalDateTime dataHora;
    private String idOnibus;
    private String idMotorista;
    
}