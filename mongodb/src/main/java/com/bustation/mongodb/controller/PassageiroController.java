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

import com.bustation.mongodb.dto.PassageiroDTO;
import com.bustation.mongodb.service.PassageiroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/passageiros")
@RequiredArgsConstructor
@Tag(name = "Passageiros", description = "Endpoints para gerenciamento de passageiros")
public class PassageiroController {

    private final PassageiroService service;

    @Operation(summary = "Lista todos os passageiros com paginação")
    @ApiResponse(responseCode = "200", description = "Lista de passageiros retornada com sucesso")
    @GetMapping
    public ResponseEntity<Page<PassageiroDTO>> listarTodos(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Operation(summary = "Busca um passageiro por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Passageiro encontrado"),
        @ApiResponse(responseCode = "404", description = "Passageiro não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PassageiroDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Cria um novo passageiro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Passageiro criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<PassageiroDTO> criar(@RequestBody PassageiroDTO dto, UriComponentsBuilder uriBuilder) {
        PassageiroDTO salvo = service.save(dto);
        URI uri = uriBuilder.path("/passageiros/{id}").buildAndExpand(salvo.id()).toUri();
        return ResponseEntity.created(uri).body(salvo);
    }

    @Operation(summary = "Atualiza um passageiro existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Passageiro atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Passageiro não encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PassageiroDTO> atualizar(@PathVariable String id, @RequestBody PassageiroDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Deleta um passageiro por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Passageiro deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Passageiro não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}