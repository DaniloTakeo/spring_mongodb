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

import com.bustation.mongodb.dto.ViagemDTO;
import com.bustation.mongodb.exception.ResourceNotFoundException;
import com.bustation.mongodb.mapper.ViagemMapper;
import com.bustation.mongodb.model.Viagem;
import com.bustation.mongodb.repository.ViagemRepository;
import com.bustation.mongodb.service.ViagemService;

@ExtendWith(MockitoExtension.class)
class ViagemServiceTest {

    @Mock
    private ViagemRepository repository;

    @Mock
    private ViagemMapper mapper;

    @InjectMocks
    private ViagemService service;

    private ViagemDTO dto;
    private Viagem entity;

    @BeforeEach
    void setUp() {
        LocalDateTime dataFutura = LocalDateTime.now().plusDays(1);

        dto = new ViagemDTO(
            "1",
            "São Paulo",
            "Rio de Janeiro",
            dataFutura,
            "onibusId1",
            "motoristaId1"
        );

        entity = new Viagem();
        entity.setId("1");
        entity.setOrigem("São Paulo");
        entity.setDestino("Rio de Janeiro");
        entity.setDataHora(dataFutura);
        entity.setIdOnibus("onibusId1");
        entity.setIdMotorista("motoristaId1");
    }

    @Test
    void deveListarTodasViagens() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Viagem> pageEntity = new PageImpl<>(List.of(entity));

        when(repository.findAll(pageable)).thenReturn(pageEntity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        Page<ViagemDTO> resultado = service.findAll(pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).id()).isEqualTo(dto.id());

        verify(repository).findAll(pageable);
        verify(mapper).toDTO(entity);
    }

    @Test
    void deveBuscarViagemPorId_quandoExistir() {
        when(repository.findById("1")).thenReturn(Optional.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        ViagemDTO resultado = service.findById("1");

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo("1");

        verify(repository).findById("1");
        verify(mapper).toDTO(entity);
    }

    @Test
    void deveLancarExcecao_quandoBuscarViagemPorId_inexistente() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById("1"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Viagem não encontrada");

        verify(repository).findById("1");
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void deveSalvarViagem() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        ViagemDTO salvo = service.save(dto);

        assertThat(salvo).isNotNull();
        assertThat(salvo.id()).isEqualTo(dto.id());

        verify(mapper).toEntity(dto);
        verify(repository).save(entity);
        verify(mapper).toDTO(entity);
    }

    @Test
    void deveAtualizarViagem_quandoExistir() {
        when(repository.existsById("1")).thenReturn(true);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        ViagemDTO atualizado = service.update("1", dto);

        assertThat(atualizado).isNotNull();
        assertThat(atualizado.id()).isEqualTo(dto.id());

        verify(repository).existsById("1");
        verify(mapper).toEntity(dto);
        verify(repository).save(entity);
        verify(mapper).toDTO(entity);
    }

    @Test
    void deveLancarExcecao_quandoAtualizarViagem_inexistente() {
        when(repository.existsById("1")).thenReturn(false);

        assertThatThrownBy(() -> service.update("1", dto))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Viagem não encontrada");

        verify(repository).existsById("1");
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void deveDeletarViagem_quandoExistir() {
        when(repository.existsById("1")).thenReturn(true);

        service.delete("1");

        verify(repository).existsById("1");
        verify(repository).deleteById("1");
    }

    @Test
    void deveLancarExcecao_quandoDeletarViagem_inexistente() {
        when(repository.existsById("1")).thenReturn(false);

        assertThatThrownBy(() -> service.delete("1"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Viagem não encontrada");

        verify(repository).existsById("1");
        verify(repository, never()).deleteById(any());
    }
}