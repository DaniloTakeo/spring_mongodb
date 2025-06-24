package com.bustation.mongodb.unit_tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.bustation.mongodb.dto.OnibusDTO;
import com.bustation.mongodb.exception.ResourceNotFoundException;
import com.bustation.mongodb.mapper.OnibusMapper;
import com.bustation.mongodb.model.Onibus;
import com.bustation.mongodb.repository.OnibusRepository;
import com.bustation.mongodb.service.OnibusService;

@ExtendWith(MockitoExtension.class)
class OnibusServiceTest {

    @InjectMocks
    private OnibusService onibusService;

    @Mock
    private OnibusRepository repository;

    @Mock
    private OnibusMapper mapper;

    private OnibusDTO dto;
    private Onibus entity;

    @BeforeEach
    void setUp() {
        dto = new OnibusDTO("1", "XYZ-1234", "Marcopolo", 42);
        entity = new Onibus("1", "XYZ-1234", "Marcopolo", 42);
    }

    @Test
    void deveRetornarTodosOnibus() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Onibus> pageEntities = new PageImpl<>(List.of(entity));

        when(repository.findAll(pageable)).thenReturn(pageEntities);
        when(mapper.toDTO(entity)).thenReturn(dto);

        Page<OnibusDTO> resultado = onibusService.findAll(pageable);

        assertEquals(1, resultado.getTotalElements());
        assertEquals(dto, resultado.getContent().get(0));
    }

    @Test
    void deveRetornarOnibusPorId() {
        when(repository.findById("1")).thenReturn(Optional.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        OnibusDTO resultado = onibusService.findById("1");

        assertEquals(dto, resultado);
    }

    @Test
    void deveLancarExcecaoQuandoOnibusNaoEncontradoPorId() {
        when(repository.findById("99")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> onibusService.findById("99"));

        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    void deveSalvarOnibus() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        OnibusDTO resultado = onibusService.save(dto);

        assertEquals(dto, resultado);
    }

    @Test
    void deveAtualizarOnibus() {
        when(repository.findById("1")).thenReturn(Optional.of(entity));
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        OnibusDTO resultado = onibusService.update("1", dto);

        assertEquals(dto, resultado);
    }

    @Test
    void deveLancarExcecaoAoAtualizarQuandoOnibusNaoExiste() {
        when(repository.findById("99")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> onibusService.update("99", dto));

        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    void deveDeletarOnibus() {
        when(repository.existsById("1")).thenReturn(true);

        assertDoesNotThrow(() -> onibusService.delete("1"));
        verify(repository).deleteById("1");
    }

    @Test
    void deveLancarExcecaoAoDeletarQuandoOnibusNaoExiste() {
        when(repository.existsById("99")).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> onibusService.delete("99"));

        assertTrue(ex.getMessage().contains("99"));
    }
}