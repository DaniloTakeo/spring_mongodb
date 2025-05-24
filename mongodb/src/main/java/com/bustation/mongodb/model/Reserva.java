package com.bustation.mongodb.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "reservas")
@Data
public class Reserva {

    @Id
    private String id;

    private String idPassageiro;
    private String idViagem;
    private LocalDateTime dataReserva;
    private int assento;

}