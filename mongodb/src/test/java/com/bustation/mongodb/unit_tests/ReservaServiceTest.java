package com.bustation.mongodb.unit_tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bustation.mongodb.dto.ReservaDTO;
import com.bustation.mongodb.mapper.ReservaMapper;
import com.bustation.mongodb.model.Reserva;
import com.bustation.mongodb.repository.ReservaRepository;
import com.bustation.mongodb.service.ReservaService;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository repository;

    @Mock
    private ReservaMapper mapper;

    @InjectMocks
    private ReservaService service;

    private ReservaDTO dto;
    private Reserva entity;

    @BeforeEach
    void setUp() {
        LocalDateTime dataFutura = LocalDateTime.now().plusDays(1);

        dto = new ReservaDTO(
            "1",
            "passageiroId1",
            "viagemId1",
            dataFutura,
            10
        );

        entity = new Reserva();
        entity.setId("1");
        entity.setIdPassageiro("passageiroId1");
        entity.setIdViagem("viagemId1");
        entity.setDataReserva(dataFutura);
        entity.setAssento(10);
    }

    @Test
    void deveListarTodasReservas() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Reserva> pageEntity = new PageImpl<>(List.of(entity));

        when(repository.findAll(pageable)).thenReturn(pageEntity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        Page<ReservaDTO> result = service.findAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).id()).isEqualTo(dto.id());

        verify(repository).findAll(pageable);
        verify(mapper).toDTO(entity);
    }

    @Test
    void deveBuscarReservaPorIdQuandoExistir() {
        when(repository.findById("1")).thenReturn(Optional.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        ReservaDTO resultado = service.findById("1");

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo("1");

        verify(repository).findById("1");
        verify(mapper).toDTO(entity);
    }

    @Test
    void deveLancarExcecaoQuandoBuscarReservaPorIdInexistente() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById("1"))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Reserva não encontrada com id: 1");

        verify(repository).findById("1");
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void deveSalvarReserva() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(any(Reserva.class))).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        ReservaDTO salvo = service.save(dto);

        assertThat(salvo).isNotNull();
        assertThat(salvo.id()).isEqualTo(dto.id());

        verify(mapper).toEntity(dto);
        verify(repository).save(any(Reserva.class));
        verify(mapper).toDTO(entity);
    }

    @Test
    void deveAtualizarReserva() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        ReservaDTO atualizado = service.update("1", dto);

        assertThat(atualizado).isNotNull();
        assertThat(atualizado.id()).isEqualTo(dto.id());

        verify(mapper).toEntity(dto);
        verify(repository).save(entity);
        verify(mapper).toDTO(entity);
    }

    @Test
    void deveDeletarReservaQuandoExistir() {
        when(repository.existsById("1")).thenReturn(true);

        service.delete("1");

        verify(repository).existsById("1");
        verify(repository).deleteById("1");
    }

    @Test
    void deveLancarExcecaoQuandoDeletarReservaInexistente() {
        when(repository.existsById("1")).thenReturn(false);

        assertThatThrownBy(() -> service.delete("1"))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Reserva não encontrada com id: 1");

        verify(repository).existsById("1");
        verify(repository, never()).deleteById(any());
    }
}