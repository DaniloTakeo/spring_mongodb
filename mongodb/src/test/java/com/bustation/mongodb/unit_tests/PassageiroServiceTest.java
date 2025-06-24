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

import com.bustation.mongodb.dto.PassageiroDTO;
import com.bustation.mongodb.exception.ResourceNotFoundException;
import com.bustation.mongodb.mapper.PassageiroMapper;
import com.bustation.mongodb.model.Passageiro;
import com.bustation.mongodb.repository.PassageiroRepository;
import com.bustation.mongodb.service.PassageiroService;

@ExtendWith(MockitoExtension.class)
class PassageiroServiceTest {

    @InjectMocks
    private PassageiroService passageiroService;

    @Mock
    private PassageiroRepository repository;

    @Mock
    private PassageiroMapper mapper;

    private PassageiroDTO dto;
    private Passageiro entity;

    @BeforeEach
    void setUp() {
        dto = new PassageiroDTO("1", "João Silva", "123.456.789-00", "joao@example.com");
        entity = new Passageiro("1", "João Silva", "123.456.789-00", "joao@example.com");
    }

    @Test
    void deveRetornarTodosPassageiros() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Passageiro> pageEntities = new PageImpl<>(List.of(entity));

        when(repository.findAll(pageable)).thenReturn(pageEntities);
        when(mapper.toDTO(entity)).thenReturn(dto);

        Page<PassageiroDTO> resultado = passageiroService.findAll(pageable);

        assertEquals(1, resultado.getTotalElements());
        assertEquals(dto, resultado.getContent().get(0));
    }

    @Test
    void deveRetornarPassageiroPorId() {
        when(repository.findById("1")).thenReturn(Optional.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        PassageiroDTO resultado = passageiroService.findById("1");

        assertEquals(dto, resultado);
    }

    @Test
    void deveLancarExcecaoQuandoPassageiroNaoEncontradoPorId() {
        when(repository.findById("99")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> passageiroService.findById("99"));

        assertTrue(ex.getMessage().contains("Passageiro não encontrado"));
    }

    @Test
    void deveSalvarPassageiro() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        PassageiroDTO resultado = passageiroService.save(dto);

        assertEquals(dto, resultado);
    }

    @Test
    void deveAtualizarPassageiro() {
        when(repository.existsById("1")).thenReturn(true);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        PassageiroDTO resultado = passageiroService.update("1", dto);

        assertEquals(dto, resultado);
        verify(entity).setId("1");
    }

    @Test
    void deveLancarExcecaoAoAtualizarQuandoPassageiroNaoExiste() {
        when(repository.existsById("99")).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> passageiroService.update("99", dto));

        assertTrue(ex.getMessage().contains("Passageiro não encontrado"));
    }

    @Test
    void deveDeletarPassageiro() {
        when(repository.existsById("1")).thenReturn(true);

        assertDoesNotThrow(() -> passageiroService.delete("1"));
        verify(repository).deleteById("1");
    }

    @Test
    void deveLancarExcecaoAoDeletarQuandoPassageiroNaoExiste() {
        when(repository.existsById("99")).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> passageiroService.delete("99"));

        assertTrue(ex.getMessage().contains("Passageiro não encontrado"));
    }
}