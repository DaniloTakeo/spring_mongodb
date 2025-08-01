package com.bustation.mongodb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bustation.mongodb.dto.MotoristaDTO;
import com.bustation.mongodb.exception.ResourceNotFoundException;
import com.bustation.mongodb.mapper.MotoristaMapper;
import com.bustation.mongodb.model.Motorista;
import com.bustation.mongodb.repository.MotoristaRepository;

import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável pela lógica de negócios relacionada à entidade {@link
 * Motorista}.
 */
@Service
@RequiredArgsConstructor
public class MotoristaService {

    /**
     * Repositório para acesso a dados da entidade Motorista.
     */
    private final MotoristaRepository repository;

    /**
     * Mapper para converter entre entidade Motorista e MotoristaDTO.
     */
    private final MotoristaMapper mapper;

    /**
     * Retorna uma página de motoristas no formato DTO.
     *
     * @param pageable as informações de paginação
     * @return página de {@link MotoristaDTO}
     */
    public Page<MotoristaDTO> findAll(final Pageable pageable) {
        return repository.findAll(pageable)
            .map(mapper::toDTO);
    }

    /**
     * Busca um motorista por ID.
     *
     * @param id o ID do motorista
     * @return o motorista encontrado no formato DTO
     * @throws ResourceNotFoundException se o motorista não for encontrado
     */
    public MotoristaDTO findById(final String id) {
        return repository.findById(id)
            .map(mapper::toDTO)
            .orElseThrow(
                () -> new ResourceNotFoundException("Motorista não"
                        + " encontrado"));
    }

    /**
     * Salva um novo motorista no banco de dados.
     *
     * @param dto o DTO do motorista a ser salvo
     * @return o motorista salvo no formato DTO
     */
    public MotoristaDTO save(final MotoristaDTO dto) {
        return mapper.toDTO(repository.save(mapper.toEntity(dto)));
    }

    /**
     * Atualiza um motorista existente com base no ID.
     *
     * @param id o ID do motorista a ser atualizado
     * @param dto os dados atualizados do motorista
     * @return o motorista atualizado no formato DTO
     * @throws ResourceNotFoundException se o motorista não for encontrado
     */
    public MotoristaDTO update(final String id, final MotoristaDTO dto) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Motorista não encontrado");
        }
        Motorista entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDTO(repository.save(entity));
    }

    /**
     * Exclui um motorista com base no ID.
     *
     * @param id o ID do motorista
     * @throws ResourceNotFoundException se o motorista não for encontrado
     */
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Motorista não encontrado");
        }
        repository.deleteById(id);
    }
}
