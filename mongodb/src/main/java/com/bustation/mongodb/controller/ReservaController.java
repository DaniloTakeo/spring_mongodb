package com.bustation.mongodb.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bustation.mongodb.model.Reserva;
import com.bustation.mongodb.service.ReservaService;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Reserva> listarTodas() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> buscarPorId(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/viagem/{idViagem}")
    public List<Reserva> buscarPorViagem(@PathVariable String idViagem) {
        return service.findByViagem(idViagem);
    }

    @GetMapping("/passageiro/{idPassageiro}")
    public List<Reserva> buscarPorPassageiro(@PathVariable String idPassageiro) {
        return service.findByPassageiro(idPassageiro);
    }

    @PostMapping
    public Reserva criar(@RequestBody Reserva reserva) {
        return service.save(reserva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> atualizar(@PathVariable String id, @RequestBody Reserva reserva) {
        return ResponseEntity.ok(service.update(id, reserva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}