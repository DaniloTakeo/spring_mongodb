package com.bustation.mongodb.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bustation.mongodb.dto.OnibusDTO;
import com.bustation.mongodb.exception.ResourceNotFoundException;
import com.bustation.mongodb.mapper.OnibusMapper;
import com.bustation.mongodb.model.Onibus;
import com.bustation.mongodb.repository.OnibusRepository;

import lombok.RequiredArgsConstructor;

/**
 * Serviço para gerenciamento das operações relacionadas à entidade Ônibus.
 *
 * Realiza operações CRUD, usando OnibusRepository para persistência e
 * OnibusMapper para conversão entre entidade e DTO.
 *
 * Utiliza cache para otimizar a busca por listas de ônibus.
 */
@Service
@RequiredArgsConstructor
public class OnibusService {

    /**
     * Repositório para operações CRUD na coleção de ônibus.
     */
    private final OnibusRepository repository;

    /**
     * Mapper para converter entre entidade Onibus e OnibusDTO.
     */
    private final OnibusMapper mapper;

    /**
     * Retorna uma página paginada de OnibusDTO.
     *
     * @param pageable parâmetros de paginação e ordenação
     * @return página com os ônibus convertidos para DTO
     */
    @Cacheable(value = "onibus")
    public Page<OnibusDTO> findAll(final Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    /**
     * Busca um ônibus pelo seu ID.
     *
     * @param id identificador do ônibus
     * @return OnibusDTO correspondente ao ID informado
     * @throws ResourceNotFoundException se o ônibus não for encontrado
     */
    public OnibusDTO findById(final String id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ônibus com ID " + id + " não encontrado"));
    }

    /**
     * Salva um novo ônibus no banco de dados.
     *
     * Limpa o cache relacionado a ônibus após a operação.
     *
     * @param onibus DTO do ônibus a ser salvo
     * @return OnibusDTO salvo com o ID gerado
     */
    @CacheEvict(value = "onibus", allEntries = true)
    public OnibusDTO save(final OnibusDTO onibus) {
        Onibus entity = mapper.toEntity(onibus);
        return mapper.toDTO(repository.save(entity));
    }

    /**
     * Atualiza os dados de um ônibus existente.
     *
     * Limpa o cache relacionado a ônibus após a operação.
     *
     * @param id identificador do ônibus a ser atualizado
     * @param dto dados do ônibus para atualização
     * @return OnibusDTO atualizado
     * @throws ResourceNotFoundException se o ônibus não for encontrado
     */
    @CacheEvict(value = "onibus", allEntries = true)
    public OnibusDTO update(final String id, final OnibusDTO dto) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ônibus com ID " + id + " não encontrado"));

        Onibus atualizado = mapper.toEntity(dto);
        atualizado.setId(id);

        return mapper.toDTO(repository.save(atualizado));
    }

    /**
     * Deleta um ônibus pelo seu ID.
     *
     * Limpa o cache relacionado a ônibus após a operação.
     *
     * @param id identificador do ônibus a ser deletado
     * @throws ResourceNotFoundException se o ônibus não existir
     */
    @CacheEvict(value = "onibus", allEntries = true)
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Ônibus com ID " + id + " não encontrado");
        }
        repository.deleteById(id);
    }
}
