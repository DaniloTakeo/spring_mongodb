package com.bustation.mongodb.controller;

import java.net.URI;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.bustation.mongodb.dto.PassageiroDTO;
import com.bustation.mongodb.service.PassageiroService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/passageiros")
@RequiredArgsConstructor
public class PassageiroController {

    private final PassageiroService service;

    @GetMapping
    public List<PassageiroDTO> listarTodos() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassageiroDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<PassageiroDTO> criar(@RequestBody PassageiroDTO dto, UriComponentsBuilder uriBuilder) {
        PassageiroDTO salvo = service.save(dto);
        URI uri = uriBuilder.path("/passageiros/{id}").buildAndExpand(salvo.id()).toUri();
        return ResponseEntity.created(uri).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassageiroDTO> atualizar(@PathVariable String id, @RequestBody PassageiroDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}