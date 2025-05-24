package com.bustation.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bustation.mongodb.model.Passageiro;

public interface PassageiroRepository extends MongoRepository<Passageiro, String> {
	
}