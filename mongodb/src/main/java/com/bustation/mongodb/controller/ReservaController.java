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

import com.bustation.mongodb.dto.ReservaDTO;
import com.bustation.mongodb.service.ReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador responsável por gerenciar reservas. Oferece endpoints
 * para criação, listagem, atualização e remoção de reservas.
 */
@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
@Tag(
    name = "Reservas",
    description = "Endpoints para gerenciamento de reservas"
)
public class ReservaController {

    /**
     * Serviço responsável pelas operações relacionadas à reserva.
     */
    private final ReservaService service;

    /**
     * Lista todas as reservas com suporte à paginação.
     *
     * @param pageable parâmetros de paginação
     * @return página contendo as reservas encontradas
     */
    @Operation(summary = "Lista todas as reservas com paginação")
    @ApiResponse(
        responseCode = "200",
        description = "Lista de reservas retornada com sucesso"
    )
    @GetMapping
    public ResponseEntity<Page<ReservaDTO>> listarTodas(
        @PageableDefault final Pageable pageable
    ) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    /**
     * Busca uma reserva pelo ID informado.
     *
     * @param id identificador da reserva
     * @return a reserva encontrada ou 404 se inexistente
     */
    @Operation(summary = "Busca uma reserva por ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Reserva encontrada"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Reserva não encontrada",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> buscarPorId(
        @PathVariable final String id
    ) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (final RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cria uma nova reserva.
     *
     * @param dto        dados da reserva
     * @param uriBuilder construtor de URI para resposta
     * @return a reserva criada com URI de localização
     */
    @Operation(summary = "Cria uma nova reserva")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Reserva criada com sucesso"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<ReservaDTO> criar(
        @RequestBody @Valid final ReservaDTO dto,
        final UriComponentsBuilder uriBuilder
    ) {
        final ReservaDTO reserva = service.save(dto);
        final URI uri = uriBuilder
            .path("/reservas/{id}")
            .buildAndExpand(reserva.id())
            .toUri();
        return ResponseEntity.created(uri).body(reserva);
    }

    /**
     * Atualiza uma reserva existente.
     *
     * @param id  identificador da reserva
     * @param dto novos dados da reserva
     * @return reserva atualizada
     */
    @Operation(summary = "Atualiza uma reserva existente")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Reserva atualizada com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Reserva não encontrada",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReservaDTO> atualizar(
        @PathVariable @Valid final String id,
        @RequestBody final ReservaDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    /**
     * Remove uma reserva pelo ID informado.
     *
     * @param id identificador da reserva
     * @return resposta sem conteúdo se sucesso, ou 404 se não encontrada
     */
    @Operation(summary = "Deleta uma reserva por ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Reserva deletada com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Reserva não encontrada",
            content = @Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable final String id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (final RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
