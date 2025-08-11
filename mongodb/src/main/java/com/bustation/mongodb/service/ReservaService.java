package com.bustation.mongodb.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bustation.mongodb.dto.ReservaDTO;
import com.bustation.mongodb.exception.ResourceNotFoundException;
import com.bustation.mongodb.mapper.ReservaMapper;
import com.bustation.mongodb.model.Reserva;
import com.bustation.mongodb.repository.ReservaRepository;

import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável por gerenciar as operações relacionadas
 * à entidade Reserva.
 */
@Service
@RequiredArgsConstructor
public class ReservaService {

    /**
     * Repositório para acesso aos dados de reserva.
     */
    private final ReservaRepository repository;

    /**
     * Mapper para conversão entre entidade e DTO de reserva.
     */
    private final ReservaMapper mapper;

    /**
     * Retorna uma página de reservas no formato DTO.
     *
     * @param pageable informações de paginação
     * @return página de ReservaDTOs
     */
    public Page<ReservaDTO> findAll(final Pageable pageable) {
        return repository.findAll(pageable)
            .map(mapper::toDTO);
    }

    /**
     * Busca uma reserva pelo seu ID.
     *
     * @param id identificador da reserva
     * @return DTO da reserva encontrada
     * @throws RuntimeException se a reserva não for encontrada
     */
    public ReservaDTO findById(final String id) {
        Reserva reserva = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Reserva não encontrada com id: " + id));
        return mapper.toDTO(reserva);
    }

    /**
     * Salva uma nova reserva com data atual.
     *
     * @param dto dados da reserva a ser criada
     * @return DTO da reserva salva
     */
    public ReservaDTO save(final ReservaDTO dto) {
        Reserva reserva = mapper.toEntity(dto);
        reserva.setDataReserva(LocalDateTime.now());
        return mapper.toDTO(repository.save(reserva));
    }

    /**
     * Atualiza uma reserva existente.
     *
     * @param id  identificador da reserva
     * @param dto dados atualizados da reserva
     * @return DTO da reserva atualizada
     */
    public ReservaDTO update(final String id, final ReservaDTO dto) {
        Reserva reserva = mapper.toEntity(dto);
        reserva.setId(id);
        return mapper.toDTO(repository.save(reserva));
    }

    /**
     * Remove uma reserva pelo seu ID.
     *
     * @param id identificador da reserva
     * @throws RuntimeException se a reserva não for encontrada
     */
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Reserva não "
                     + "encontrada com id: " + id);
        }
        repository.deleteById(id);
    }
}
