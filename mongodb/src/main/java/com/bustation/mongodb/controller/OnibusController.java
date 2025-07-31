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

/**
 * Controlador REST responsável por gerenciar os recursos relacionados
 * à entidade Ônibus. Disponibiliza endpoints para operações de CRUD.
 */
@RestController
@RequestMapping("/onibus")
@RequiredArgsConstructor
@Tag(name = "Ônibus",
     description = "Endpoints para gerenciamento de ônibus")
public class OnibusController {

    /**
     * Serviço responsável pelas regras de negócio relacionadas a ônibus.
     */
    private final OnibusService service;

    /**
     * Lista todos os ônibus com suporte à paginação.
     *
     * @param pageable parâmetros de paginação
     * @return página contendo os ônibus encontrados
     */
    @Operation(summary = "Lista todos os ônibus com paginação")
    @ApiResponse(responseCode = "200",
                 description = "Lista de ônibus retornada com sucesso")
    @GetMapping
    public ResponseEntity<Page<OnibusDTO>> listarTodos(
        @PageableDefault final Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    /**
     * Busca um ônibus pelo identificador.
     *
     * @param id identificador do ônibus
     * @return ônibus correspondente ao ID fornecido
     */
    @Operation(summary = "Busca ônibus por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ônibus encontrado"),
        @ApiResponse(responseCode = "404",
            description = "Ônibus não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OnibusDTO> buscarPorId(
        @PathVariable final String id) {
        final OnibusDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Cria um novo registro de ônibus.
     *
     * @param onibus objeto contendo os dados do ônibus
     * @param uriBuilder construtor de URI para resposta
     * @return resposta com status 201 e localização do recurso
     */
    @Operation(summary = "Cria um novo ônibus")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",
                     description = "Ônibus criado com sucesso"),
        @ApiResponse(responseCode = "400",
                     description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<OnibusDTO> criar(
        @RequestBody @Valid final OnibusDTO onibus,
        final UriComponentsBuilder uriBuilder) {
        final OnibusDTO onibusSalvo = service.save(onibus);
        final URI uri = uriBuilder.path("/onibus/{id}")
            .buildAndExpand(onibusSalvo.id()).toUri();
        return ResponseEntity.created(uri).body(onibusSalvo);
    }

    /**
     * Atualiza um ônibus existente com base no ID fornecido.
     *
     * @param id identificador do ônibus a ser atualizado
     * @param onibus dados atualizados do ônibus
     * @return ônibus atualizado
     */
    @Operation(summary = "Atualiza um ônibus existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Ônibus atualizado com sucesso"),
        @ApiResponse(responseCode = "404",
                     description = "Ônibus não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OnibusDTO> atualizar(
        @PathVariable final String id,
        @RequestBody @Valid final OnibusDTO onibus) {
        final OnibusDTO atualizado = service.update(id, onibus);
        return ResponseEntity.ok(atualizado);
    }

    /**
     * Exclui um ônibus com base no ID fornecido.
     *
     * @param id identificador do ônibus a ser deletado
     * @return resposta com status 204 (sem conteúdo)
     */
    @Operation(summary = "Deleta um ônibus por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204",
                     description = "Ônibus deletado com sucesso"),
        @ApiResponse(responseCode = "404",
                     description = "Ônibus não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
        @PathVariable final String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
