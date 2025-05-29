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

import com.bustation.mongodb.dto.ViagemDTO;
import com.bustation.mongodb.service.ViagemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/viagens")
@RequiredArgsConstructor
public class ViagemController {

    private final ViagemService service;

    @GetMapping
    public List<ViagemDTO> listarTodos() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViagemDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ViagemDTO> criar(@RequestBody ViagemDTO dto, UriComponentsBuilder uriBuilder) {
        ViagemDTO salvo = service.save(dto);
        URI uri = uriBuilder.path("/viagens/{id}").buildAndExpand(salvo.id()).toUri();
        return ResponseEntity.created(uri).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ViagemDTO> atualizar(@PathVariable String id, @RequestBody ViagemDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}