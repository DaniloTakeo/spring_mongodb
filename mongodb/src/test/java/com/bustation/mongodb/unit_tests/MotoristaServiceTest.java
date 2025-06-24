package com.bustation.mongodb.unit_tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.bustation.mongodb.dto.MotoristaDTO;
import com.bustation.mongodb.exception.ResourceNotFoundException;
import com.bustation.mongodb.mapper.MotoristaMapper;
import com.bustation.mongodb.model.Motorista;
import com.bustation.mongodb.repository.MotoristaRepository;
import com.bustation.mongodb.service.MotoristaService;

@ExtendWith(MockitoExtension.class)
class MotoristaServiceTest {

    @InjectMocks
    private MotoristaService motoristaService;

    @Mock
    private MotoristaRepository repository;

    @Mock
    private MotoristaMapper mapper;

    private MotoristaDTO dto;
    private Motorista entity;

    @BeforeEach
    void setUp() {
        dto = new MotoristaDTO("1", "João", "123456789", "D");
        entity = new Motorista("1", "João", "123456789", "D");
    }

    @Test
    void deveRetornarTodosMotoristas() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Motorista> pageEntities = new PageImpl<>(List.of(entity));

        when(repository.findAll(pageable)).thenReturn(pageEntities);
        when(mapper.toDTO(entity)).thenReturn(dto);

        Page<MotoristaDTO> resultado = motoristaService.findAll(pageable);

        assertEquals(1, resultado.getTotalElements());
        assertEquals(dto, resultado.getContent().get(0));
    }

    @Test
    void deveRetornarMotoristaPorId() {
        when(repository.findById("1")).thenReturn(Optional.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        MotoristaDTO resultado = motoristaService.findById("1");

        assertEquals(dto, resultado);
    }

    @Test
    void deveLancarExcecaoQuandoMotoristaNaoEncontradoPorId() {
        when(repository.findById("99")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> motoristaService.findById("99"));
    }

    @Test
    void deveSalvarMotorista() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        MotoristaDTO resultado = motoristaService.save(dto);

        assertEquals(dto, resultado);
    }

    @Test
    void deveAtualizarMotorista() {
        when(repository.existsById("1")).thenReturn(true);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        MotoristaDTO resultado = motoristaService.update("1", dto);

        assertEquals(dto, resultado);
    }

    @Test
    void deveLancarExcecaoAoAtualizarMotoristaNaoExiste() {
        when(repository.existsById("99")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> motoristaService.update("99", dto));
    }

    @Test
    void deveDeletarMotorista() {
        when(repository.existsById("1")).thenReturn(true);

        assertDoesNotThrow(() -> motoristaService.delete("1"));
        verify(repository).deleteById("1");
    }

    @Test
    void deveLancarExcecaoAoDeletarMotoristaNaoExiste() {
        when(repository.existsById("99")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> motoristaService.delete("99"));
    }
}