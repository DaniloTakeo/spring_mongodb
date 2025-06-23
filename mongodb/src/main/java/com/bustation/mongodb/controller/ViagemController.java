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

import com.bustation.mongodb.dto.ViagemDTO;
import com.bustation.mongodb.service.ViagemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/viagens")
@RequiredArgsConstructor
@Tag(name = "Viagens", description = "Endpoints para gerenciamento de viagens")
public class ViagemController {

    private final ViagemService service;

    @Operation(summary = "Lista todas as viagens com paginação")
    @ApiResponse(responseCode = "200", description = "Lista de viagens retornada com sucesso")
    @GetMapping
    public ResponseEntity<Page<ViagemDTO>> listarTodos(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Operation(summary = "Busca uma viagem por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Viagem encontrada"),
        @ApiResponse(responseCode = "404", description = "Viagem não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ViagemDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Cria uma nova viagem")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Viagem criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ViagemDTO> criar(@RequestBody @Valid ViagemDTO dto, UriComponentsBuilder uriBuilder) {
        ViagemDTO salvo = service.save(dto);
        URI uri = uriBuilder.path("/viagens/{id}").buildAndExpand(salvo.id()).toUri();
        return ResponseEntity.created(uri).body(salvo);
    }

    @Operation(summary = "Atualiza uma viagem existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Viagem atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Viagem não encontrada", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ViagemDTO> atualizar(@PathVariable @Valid String id, @RequestBody ViagemDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Deleta uma viagem por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Viagem deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Viagem não encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}