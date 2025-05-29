package com.bustation.mongodb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bustation.mongodb.dto.PassageiroDTO;
import com.bustation.mongodb.exception.ResourceNotFoundException;
import com.bustation.mongodb.mapper.PassageiroMapper;
import com.bustation.mongodb.model.Passageiro;
import com.bustation.mongodb.repository.PassageiroRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PassageiroService {

    private final PassageiroRepository repository;
    private final PassageiroMapper mapper;

    public List<PassageiroDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public PassageiroDTO findById(String id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Passageiro não encontrado"));
    }

    public PassageiroDTO save(PassageiroDTO dto) {
        return mapper.toDTO(repository.save(mapper.toEntity(dto)));
    }

    public PassageiroDTO update(String id, PassageiroDTO dto) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Passageiro não encontrado");
        }
        Passageiro entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDTO(repository.save(entity));
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Passageiro não encontrado");
        }
        repository.deleteById(id);
    }
}