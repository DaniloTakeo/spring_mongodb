package com.bustation.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bustation.mongodb.model.Onibus;

/**
 * Repositório para operações com a entidade Onibus.
 */
public interface OnibusRepository
    extends MongoRepository<Onibus, String> {
}
