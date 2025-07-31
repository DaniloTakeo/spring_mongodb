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

import com.bustation.mongodb.dto.MotoristaDTO;
import com.bustation.mongodb.service.MotoristaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para gerenciamento dos motoristas do sistema.
 *
 * Oferece endpoints para operações de CRUD como:
 * listagem, criação, busca, atualização e exclusão de motoristas.
 */
@RestController
@RequestMapping("/motoristas")
@RequiredArgsConstructor
@Tag(
    name = "Motoristas",
    description = "Endpoints para gerenciamento de motoristas"
)
public class MotoristaController {

    /**
     * Serviço responsável pela regra de negócio de Motorista.
     */
    private final MotoristaService service;

    /**
     * Lista todos os motoristas cadastrados com suporte à paginação.
     *
     * @param pageable informações de paginação
     * @return página contendo os motoristas
     */
    @Operation(summary = "Lista todos os motoristas com paginação")
    @ApiResponse(
        responseCode = "200",
        description = "Lista retornada com sucesso"
    )
    @GetMapping
    public ResponseEntity<Page<MotoristaDTO>> listarTodos(
        @PageableDefault final Pageable pageable
    ) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    /**
     * Busca um motorista específico pelo seu ID.
     *
     * @param id identificador do motorista
     * @return dados do motorista encontrado
     */
    @Operation(summary = "Busca motorista por ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Motorista encontrado"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Motorista não encontrado"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<MotoristaDTO> buscarPorId(@PathVariable final
            String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Cria um novo motorista com os dados fornecidos.
     *
     * @param dto        dados do motorista
     * @param uriBuilder construtor de URI
     * @return motorista criado com URI no header
     */
    @Operation(summary = "Cria um novo motorista")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Motorista criado com sucesso"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos"
        )
    })
    @PostMapping
    public ResponseEntity<MotoristaDTO> criar(
        @RequestBody @Valid final MotoristaDTO dto,
        final UriComponentsBuilder uriBuilder
    ) {
        MotoristaDTO salvo = service.save(dto);
        URI uri = uriBuilder
            .path("/motoristas/{id}")
            .buildAndExpand(salvo.id())
            .toUri();
        return ResponseEntity.created(uri).body(salvo);
    }

    /**
     * Atualiza os dados de um motorista existente.
     *
     * @param id  identificador do motorista
     * @param dto novos dados do motorista
     * @return motorista atualizado
     */
    @Operation(summary = "Atualiza os dados de um motorista existente")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Motorista atualizado com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Motorista não encontrado"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<MotoristaDTO> atualizar(
        @PathVariable final String id,
        @RequestBody @Valid final MotoristaDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    /**
     * Remove um motorista com base no seu ID.
     *
     * @param id identificador do motorista
     * @return resposta vazia com status 204
     */
    @Operation(summary = "Remove um motorista pelo ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Motorista deletado com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Motorista não encontrado"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable final String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
