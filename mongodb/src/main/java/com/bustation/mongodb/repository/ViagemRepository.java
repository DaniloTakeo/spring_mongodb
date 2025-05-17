package com.bustation.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bustation.mongodb.model.Viagem;

public interface ViagemRepository extends MongoRepository<Viagem, String> {
	
}