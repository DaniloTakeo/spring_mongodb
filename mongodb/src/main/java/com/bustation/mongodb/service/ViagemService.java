package com.bustation.mongodb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bustation.mongodb.dto.ViagemDTO;
import com.bustation.mongodb.exception.ResourceNotFoundException;
import com.bustation.mongodb.mapper.ViagemMapper;
import com.bustation.mongodb.model.Viagem;
import com.bustation.mongodb.repository.ViagemRepository;

import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável por gerenciar as operações relacionadas
 * à entidade Viagem.
 */
@Service
@RequiredArgsConstructor
public class ViagemService {

    /**
     * Repositório para acesso aos dados de viagem.
     */
    private final ViagemRepository repository;

    /**
     * Mapper para conversão entre entidade e DTO de viagem.
     */
    private final ViagemMapper mapper;

    /**
     * Retorna uma página de viagens no formato DTO.
     *
     * @param pageable informações de paginação
     * @return página de ViagemDTOs
     */
    public Page<ViagemDTO> findAll(final Pageable pageable) {
        return repository.findAll(pageable)
            .map(mapper::toDTO);
    }

    /**
     * Busca uma viagem pelo seu ID.
     *
     * @param id identificador da viagem
     * @return DTO da viagem encontrada
     * @throws ResourceNotFoundException se a viagem não for encontrada
     */
    public ViagemDTO findById(final String id) {
        return repository.findById(id)
            .map(mapper::toDTO)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Viagem não encontrada"));
    }

    /**
     * Salva uma nova viagem.
     *
     * @param dto dados da viagem a ser criada
     * @return DTO da viagem salva
     */
    public ViagemDTO save(final ViagemDTO dto) {
        return mapper.toDTO(repository.save(mapper.toEntity(dto)));
    }

    /**
     * Atualiza uma viagem existente.
     *
     * @param id  identificador da viagem
     * @param dto dados atualizados da viagem
     * @return DTO da viagem atualizada
     * @throws ResourceNotFoundException se a viagem não existir
     */
    public ViagemDTO update(final String id, final ViagemDTO dto) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Viagem não encontrada");
        }
        Viagem entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDTO(repository.save(entity));
    }

    /**
     * Remove uma viagem pelo seu ID.
     *
     * @param id identificador da viagem
     * @throws ResourceNotFoundException se a viagem não existir
     */
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Viagem não encontrada");
        }
        repository.deleteById(id);
    }
}
