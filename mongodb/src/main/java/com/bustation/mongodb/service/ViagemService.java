package com.bustation.mongodb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bustation.mongodb.dto.ViagemDTO;
import com.bustation.mongodb.exception.ResourceNotFoundException;
import com.bustation.mongodb.mapper.ViagemMapper;
import com.bustation.mongodb.model.Viagem;
import com.bustation.mongodb.repository.ViagemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViagemService {

    private final ViagemRepository repository;
    private final ViagemMapper mapper;

    public List<ViagemDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public ViagemDTO findById(String id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Viagem não encontrada"));
    }

    public ViagemDTO save(ViagemDTO dto) {
        return mapper.toDTO(repository.save(mapper.toEntity(dto)));
    }

    public ViagemDTO update(String id, ViagemDTO dto) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Viagem não encontrada");
        }
        Viagem entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDTO(repository.save(entity));
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Viagem não encontrada");
        }
        repository.deleteById(id);
    }
}