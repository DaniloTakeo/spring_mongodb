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

@Service
@RequiredArgsConstructor
public class MotoristaService {

    private final MotoristaRepository repository;
    private final MotoristaMapper mapper;

    public Page<MotoristaDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    public MotoristaDTO findById(String id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Motorista não encontrado"));
    }

    public MotoristaDTO save(MotoristaDTO dto) {
        return mapper.toDTO(repository.save(mapper.toEntity(dto)));
    }

    public MotoristaDTO update(String id, MotoristaDTO dto) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Motorista não encontrado");
        }
        Motorista entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDTO(repository.save(entity));
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Motorista não encontrado");
        }
        repository.deleteById(id);
    }
}