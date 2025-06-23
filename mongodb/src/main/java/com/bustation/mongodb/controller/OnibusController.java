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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/onibus")
@RequiredArgsConstructor
@Tag(name = "Ônibus", description = "Endpoints para gerenciamento de ônibus")
public class OnibusController {

    private final OnibusService service;

    @Operation(summary = "Lista todos os ônibus com paginação")
    @ApiResponse(responseCode = "200", description = "Lista de ônibus retornada com sucesso")
    @GetMapping
    public ResponseEntity<Page<OnibusDTO>> listarTodos(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Operation(summary = "Busca ônibus por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ônibus encontrado"),
        @ApiResponse(responseCode = "404", description = "Ônibus não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OnibusDTO> buscarPorId(@PathVariable String id) {
        OnibusDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Cria um novo ônibus")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ônibus criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<OnibusDTO> criar(@RequestBody @Valid OnibusDTO onibus, UriComponentsBuilder uriBuilder) {
        OnibusDTO onibusSalvo = service.save(onibus);
        URI uri = uriBuilder.path("/onibus/{id}").buildAndExpand(onibusSalvo.id()).toUri();
        return ResponseEntity.created(uri).body(onibusSalvo);
    }

    @Operation(summary = "Atualiza um ônibus existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ônibus atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Ônibus não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OnibusDTO> atualizar(@PathVariable String id, @RequestBody @Valid OnibusDTO onibus) {
        OnibusDTO atualizado = service.update(id, onibus);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Deleta um ônibus por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Ônibus deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Ônibus não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}