package com.bustation.mongodb.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import com.bustation.mongodb.dto.OnibusDTO;
import com.bustation.mongodb.service.OnibusService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/onibus")
@RequiredArgsConstructor
public class OnibusController {

    private final OnibusService service;

    @GetMapping
    public ResponseEntity<Page<OnibusDTO>> listarTodos(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OnibusDTO> buscarPorId(@PathVariable String id) {
        OnibusDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<OnibusDTO> criar(@RequestBody @Valid OnibusDTO onibus, UriComponentsBuilder uriBuilder) {
        OnibusDTO onibusSalvo = service.save(onibus);
        URI uri = uriBuilder.path("/onibus/{id}").buildAndExpand(onibusSalvo.id()).toUri();
        return ResponseEntity.created(uri).body(onibusSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OnibusDTO> atualizar(@PathVariable String id, @RequestBody @Valid OnibusDTO onibus) {
        OnibusDTO atualizado = service.update(id, onibus);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}