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

import com.bustation.mongodb.dto.MotoristaDTO;
import com.bustation.mongodb.service.MotoristaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/motoristas")
@RequiredArgsConstructor
public class MotoristaController {

    private final MotoristaService service;

    @GetMapping
    public List<MotoristaDTO> listarTodos() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotoristaDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<MotoristaDTO> criar(@RequestBody MotoristaDTO dto, UriComponentsBuilder uriBuilder) {
        MotoristaDTO salvo = service.save(dto);
        URI uri = uriBuilder.path("/motoristas/{id}").buildAndExpand(salvo.id()).toUri();
        return ResponseEntity.created(uri).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotoristaDTO> atualizar(@PathVariable String id, @RequestBody MotoristaDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}