package com.bustation.mongodb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bustation.mongodb.model.Reserva;
import com.bustation.mongodb.repository.ReservaRepository;

@Service
public class ReservaService {

    private final ReservaRepository repository;

    public ReservaService(ReservaRepository repository) {
        this.repository = repository;
    }

    public List<Reserva> findAll() {
        return repository.findAll();
    }

    public Optional<Reserva> findById(String id) {
        return repository.findById(id);
    }

    public List<Reserva> findByViagem(String idViagem) {
        return repository.findByIdViagem(idViagem);
    }

    public List<Reserva> findByPassageiro(String idPassageiro) {
        return repository.findByIdPassageiro(idPassageiro);
    }

    public Reserva save(Reserva reserva) {
        return repository.save(reserva);
    }

    public Reserva update(String id, Reserva atualizado) {
        atualizado.setId(id);
        return repository.save(atualizado);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}