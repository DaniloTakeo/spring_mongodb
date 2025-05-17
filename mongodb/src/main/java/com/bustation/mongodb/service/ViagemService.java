package com.bustation.mongodb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bustation.mongodb.model.Viagem;
import com.bustation.mongodb.repository.ViagemRepository;

@Service
public class ViagemService {

    private final ViagemRepository repository;

    public ViagemService(ViagemRepository repository) {
        this.repository = repository;
    }

    public List<Viagem> listarTodas() {
        return repository.findAll();
    }

    public Optional<Viagem> buscarPorId(String id) {
        return repository.findById(id);
    }

    public Viagem salvar(Viagem viagem) {
        return repository.save(viagem);
    }

    public void deletar(String id) {
        repository.deleteById(id);
    }
}