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

import com.bustation.mongodb.model.Onibus;
import com.bustation.mongodb.service.OnibusService;

@RestController
@RequestMapping("/onibus")
public class OnibusController {

    private final OnibusService service;

    public OnibusController(OnibusService service) {
        this.service = service;
    }

    @GetMapping
    public List<Onibus> listarTodos() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Onibus> buscarPorId(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Onibus criar(@RequestBody Onibus onibus) {
        return service.save(onibus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Onibus> atualizar(@PathVariable String id, @RequestBody Onibus onibus) {
        return ResponseEntity.ok(service.update(id, onibus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}