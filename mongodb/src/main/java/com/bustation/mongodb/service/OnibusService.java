package com.bustation.mongodb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bustation.mongodb.dto.OnibusDTO;
import com.bustation.mongodb.exception.ResourceNotFoundException;
import com.bustation.mongodb.mapper.OnibusMapper;
import com.bustation.mongodb.model.Onibus;
import com.bustation.mongodb.repository.OnibusRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OnibusService {

    private final OnibusRepository repository;
    private final OnibusMapper mapper;

    public Page<OnibusDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    public OnibusDTO findById(String id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Ônibus com ID " + id + " não encontrado"));
    }

    public OnibusDTO save(OnibusDTO onibus) {
        Onibus entity = mapper.toEntity(onibus);
        return mapper.toDTO(repository.save(entity));
    }

    public OnibusDTO update(String id, OnibusDTO dto) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ônibus com ID " + id + " não encontrado"));

        Onibus atualizado = mapper.toEntity(dto);
        atualizado.setId(id);

        return mapper.toDTO(repository.save(atualizado));
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Ônibus com ID " + id + " não encontrado");
        }
        repository.deleteById(id);
    }
}