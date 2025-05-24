package com.bustation.mongodb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bustation.mongodb.model.Motorista;
import com.bustation.mongodb.repository.MotoristaRepository;

@Service
public class MotoristaService {

    private final MotoristaRepository repository;

    public MotoristaService(MotoristaRepository repository) {
        this.repository = repository;
    }

    public List<Motorista> findAll() {
        return repository.findAll();
    }

    public Optional<Motorista> findById(String id) {
        return repository.findById(id);
    }

    public Motorista save(Motorista motorista) {
        return repository.save(motorista);
    }

    public Motorista update(String id, Motorista atualizado) {
        atualizado.setId(id);
        return repository.save(atualizado);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}