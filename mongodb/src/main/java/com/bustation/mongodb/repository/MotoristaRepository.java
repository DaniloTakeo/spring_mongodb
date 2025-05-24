package com.bustation.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bustation.mongodb.model.Motorista;

public interface MotoristaRepository extends MongoRepository<Motorista, String> {
	
}
