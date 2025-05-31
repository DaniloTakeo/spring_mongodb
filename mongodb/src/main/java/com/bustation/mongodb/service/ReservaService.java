package com.bustation.mongodb.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bustation.mongodb.dto.ReservaDTO;
import com.bustation.mongodb.mapper.ReservaMapper;
import com.bustation.mongodb.model.Reserva;
import com.bustation.mongodb.repository.ReservaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository repository;
    private final ReservaMapper mapper;

    public Page<ReservaDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    public ReservaDTO findById(String id) {
        Reserva reserva = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada com id: " + id));
        return mapper.toDTO(reserva);
    }

    public ReservaDTO save(ReservaDTO dto) {
        Reserva reserva = mapper.toEntity(dto);
        reserva.setDataReserva(LocalDateTime.now()); // define a data da reserva como o momento atual
        return mapper.toDTO(repository.save(reserva));
    }

    public ReservaDTO update(String id, ReservaDTO dto) {
        Reserva reserva = mapper.toEntity(dto);
        reserva.setId(id);
        return mapper.toDTO(repository.save(reserva));
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Reserva não encontrada com id: " + id);
        }
        repository.deleteById(id);
    }
}