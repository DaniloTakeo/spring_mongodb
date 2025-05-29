package com.bustation.mongodb.service;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<ReservaDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
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