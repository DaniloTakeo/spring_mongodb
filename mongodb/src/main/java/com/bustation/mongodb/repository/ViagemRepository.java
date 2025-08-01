package com.bustation.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bustation.mongodb.model.Viagem;

/**
 * Repositório para operações com a entidade Viagem.
 */
public interface ViagemRepository
    extends MongoRepository<Viagem, String> {
}
