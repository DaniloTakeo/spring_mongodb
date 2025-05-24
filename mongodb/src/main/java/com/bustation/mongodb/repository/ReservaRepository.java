package com.bustation.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bustation.mongodb.model.Reserva;

public interface ReservaRepository extends MongoRepository<Reserva, String> {
    List<Reserva> findByIdViagem(String idViagem);
    List<Reserva> findByIdPassageiro(String idPassageiro);
}