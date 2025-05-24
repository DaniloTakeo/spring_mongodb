package com.bustation.mongodb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bustation.mongodb.model.Passageiro;
import com.bustation.mongodb.repository.PassageiroRepository;

@Service
public class PassageiroService {

    private final PassageiroRepository repository;

    public PassageiroService(PassageiroRepository repository) {
        this.repository = repository;
    }

    public List<Passageiro> findAll() {
        return repository.findAll();
    }

    public Optional<Passageiro> findById(String id) {
        return repository.findById(id);
    }

    public Passageiro save(Passageiro passageiro) {
        return repository.save(passageiro);
    }

    public Passageiro update(String id, Passageiro atualizado) {
        atualizado.setId(id);
        return repository.save(atualizado);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}