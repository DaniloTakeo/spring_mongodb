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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para gerenciar passageiros.
 *
 * Este controlador fornece endpoints para operações CRUD sobre passageiros,
 * incluindo listagem com paginação, busca por ID, criação, atualização e
 * exclusão.
 */
@RestController
@RequestMapping("/passageiros")
@RequiredArgsConstructor
@Tag(name = "Passageiros",
    description = "Endpoints para gerenciamento de passageiros")
public class PassageiroController {

    /**
     * Serviço responsável pela lógica de negócios relacionada a passageiros.
     */
    private final PassageiroService service;

    /**
     * Lista todos os passageiros com suporte a paginação.
     *
     * @param pageable informações de paginação.
     * @return lista paginada de passageiros.
     */
    @Operation(summary = "Lista todos os passageiros com paginação")
    @ApiResponse(
        responseCode = "200",
        description = "Lista de passageiros retornada com sucesso"
    )
    @GetMapping
    public ResponseEntity<Page<PassageiroDTO>> listarTodos(
        @PageableDefault final Pageable pageable
    ) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    /**
     * Busca um passageiro específico por ID.
     *
     * @param id identificador do passageiro.
     * @return passageiro correspondente ao ID fornecido.
     */
    @Operation(summary = "Busca um passageiro por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "Passageiro encontrado"),
        @ApiResponse(
            responseCode = "404",
            description = "Passageiro não encontrado",
            content = @io.swagger.v3.oas.annotations.media.Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<PassageiroDTO> buscarPorId(
        @PathVariable final String id
    ) {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Cria um novo passageiro.
     *
     * @param dto dados do novo passageiro.
     * @param uriBuilder construtor de URI.
     * @return passageiro criado com URI no cabeçalho Location.
     */
    @Operation(summary = "Cria um novo passageiro")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Passageiro criado com sucesso"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos",
            content = @io.swagger.v3.oas.annotations.media.Content
        )
    })
    @PostMapping
    public ResponseEntity<PassageiroDTO> criar(
        @RequestBody @Valid final PassageiroDTO dto,
        final UriComponentsBuilder uriBuilder
    ) {
        final PassageiroDTO salvo = service.save(dto);
        final URI uri = uriBuilder
            .path("/passageiros/{id}")
            .buildAndExpand(salvo.id())
            .toUri();
        return ResponseEntity.created(uri).body(salvo);
    }

    /**
     * Atualiza um passageiro existente.
     *
     * @param id identificador do passageiro.
     * @param dto dados atualizados do passageiro.
     * @return passageiro atualizado.
     */
    @Operation(summary = "Atualiza um passageiro existente")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Passageiro atualizado com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Passageiro não encontrado",
            content = @io.swagger.v3.oas.annotations.media.Content
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<PassageiroDTO> atualizar(
        @PathVariable final String id,
        @RequestBody @Valid final PassageiroDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    /**
     * Exclui um passageiro pelo ID.
     *
     * @param id identificador do passageiro.
     * @return resposta HTTP 204 se deletado com sucesso.
     */
    @Operation(summary = "Deleta um passageiro por ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Passageiro deletado com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Passageiro não encontrado",
            content = @io.swagger.v3.oas.annotations.media.Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
        @PathVariable final String id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
