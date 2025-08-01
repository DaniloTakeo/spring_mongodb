package com.bustation.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bustation.mongodb.model.Passageiro;

/**
 * Repositório para operações com a entidade Passageiro.
 */
public interface PassageiroRepository
    extends MongoRepository<Passageiro, String> {
}
