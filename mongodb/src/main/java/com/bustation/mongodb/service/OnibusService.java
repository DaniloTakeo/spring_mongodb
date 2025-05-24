package com.bustation.mongodb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bustation.mongodb.model.Onibus;
import com.bustation.mongodb.repository.OnibusRepository;

@Service
public class OnibusService {

    private final OnibusRepository repository;

    public OnibusService(OnibusRepository repository) {
        this.repository = repository;
    }

    public List<Onibus> findAll() {
        return repository.findAll();
    }

    public Optional<Onibus> findById(String id) {
        return repository.findById(id);
    }

    public Onibus save(Onibus onibus) {
        return repository.save(onibus);
    }

    public Onibus update(String id, Onibus atualizado) {
        atualizado.setId(id);
        return repository.save(atualizado);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}