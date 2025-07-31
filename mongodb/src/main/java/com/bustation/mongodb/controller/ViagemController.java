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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador responsável por expor endpoints REST para operações relacionadas
 * à entidade Viagem.
 */
@RestController
@RequestMapping("/viagens")
@RequiredArgsConstructor
@Tag(
    name = "Viagens",
    description = "Endpoints para gerenciamento de viagens"
)
public class ViagemController {

    /**
     * Serviço de operações de negócio para Viagem.
     */
    private final ViagemService service;

    /**
     * Lista todas as viagens com suporte a paginação.
     *
     * @param pageable informações de paginação
     * @return página com viagens encontradas
     */
    @Operation(summary = "Lista todas as viagens com paginação")
    @ApiResponse(
        responseCode = "200",
        description = "Lista de viagens retornada com sucesso"
    )
    @GetMapping
    public ResponseEntity<Page<ViagemDTO>> listarTodos(
        final @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    /**
     * Busca uma viagem pelo seu ID.
     *
     * @param id identificador da viagem
     * @return viagem correspondente ao ID
     */
    @Operation(summary = "Busca uma viagem por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Viagem encontrada"),
        @ApiResponse(
            responseCode = "404",
            description = "Viagem não encontrada",
            content = @io.swagger.v3.oas.annotations.media.Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ViagemDTO> buscarPorId(
        final @PathVariable String id
    ) {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Cria uma nova viagem.
     *
     * @param dto objeto de transferência de dados da viagem
     * @param uriBuilder construtor de URI para retorno da localização
     * @return viagem criada com código 201 e URI no header
     */
    @Operation(summary = "Cria uma nova viagem")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Viagem criada com sucesso"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos",
            content = @io.swagger.v3.oas.annotations.media.Content
        )
    })
    @PostMapping
    public ResponseEntity<ViagemDTO> criar(
        final @RequestBody @Valid ViagemDTO dto,
        final UriComponentsBuilder uriBuilder
    ) {
        final ViagemDTO salvo = service.save(dto);
        final URI uri = uriBuilder
            .path("/viagens/{id}")
            .buildAndExpand(salvo.id())
            .toUri();
        return ResponseEntity.created(uri).body(salvo);
    }

    /**
     * Atualiza uma viagem existente pelo ID.
     *
     * @param id identificador da viagem
     * @param dto objeto de transferência atualizado
     * @return viagem atualizada
     */
    @Operation(summary = "Atualiza uma viagem existente")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Viagem atualizada com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Viagem não encontrada",
            content = @io.swagger.v3.oas.annotations.media.Content
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ViagemDTO> atualizar(
        final @PathVariable @Valid String id,
        final @RequestBody ViagemDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    /**
     * Deleta uma viagem pelo ID.
     *
     * @param id identificador da viagem
     * @return resposta com status 204 em caso de sucesso
     */
    @Operation(summary = "Deleta uma viagem por ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Viagem deletada com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Viagem não encontrada",
            content = @io.swagger.v3.oas.annotations.media.Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(final @PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
