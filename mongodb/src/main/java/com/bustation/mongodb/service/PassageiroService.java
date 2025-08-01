package com.bustation.mongodb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bustation.mongodb.dto.PassageiroDTO;
import com.bustation.mongodb.exception.ResourceNotFoundException;
import com.bustation.mongodb.mapper.PassageiroMapper;
import com.bustation.mongodb.model.Passageiro;
import com.bustation.mongodb.repository.PassageiroRepository;

import lombok.RequiredArgsConstructor;

/**
 * Serviço para gerenciamento das operações relacionadas à entidade Passageiro.
 *
 * Realiza operações CRUD, usando PassageiroRepository para persistência e
 * PassageiroMapper para conversão entre entidade e DTO.
 */
@Service
@RequiredArgsConstructor
public class PassageiroService {

    /**
     * Repositório para operações CRUD na coleção de passageiros.
     */
    private final PassageiroRepository repository;

    /**
     * Mapper para converter entre entidade Passageiro e PassageiroDTO.
     */
    private final PassageiroMapper mapper;

    /**
     * Retorna uma página paginada de PassageiroDTO.
     *
     * @param pageable parâmetros de paginação e ordenação
     * @return página com os passageiros convertidos para DTO
     */
    public Page<PassageiroDTO> findAll(final Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    /**
     * Busca um passageiro pelo seu ID.
     *
     * @param id identificador do passageiro
     * @return PassageiroDTO correspondente ao ID informado
     * @throws ResourceNotFoundException se o passageiro não for encontrado
     */
    public PassageiroDTO findById(final String id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Passageiro não encontrado"));
    }

    /**
     * Salva um novo passageiro no banco de dados.
     *
     * @param dto DTO do passageiro a ser salvo
     * @return PassageiroDTO salvo com o ID gerado
     */
    public PassageiroDTO save(final PassageiroDTO dto) {
        return mapper.toDTO(repository.save(mapper.toEntity(dto)));
    }

    /**
     * Atualiza os dados de um passageiro existente.
     *
     * @param id identificador do passageiro a ser atualizado
     * @param dto dados do passageiro para atualização
     * @return PassageiroDTO atualizado
     * @throws ResourceNotFoundException se o passageiro não existir
     */
    public PassageiroDTO update(final String id, final PassageiroDTO dto) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Passageiro não encontrado");
        }
        Passageiro entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDTO(repository.save(entity));
    }

    /**
     * Deleta um passageiro pelo seu ID.
     *
     * @param id identificador do passageiro a ser deletado
     * @throws ResourceNotFoundException se o passageiro não existir
     */
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Passageiro não encontrado");
        }
        repository.deleteById(id);
    }
}
