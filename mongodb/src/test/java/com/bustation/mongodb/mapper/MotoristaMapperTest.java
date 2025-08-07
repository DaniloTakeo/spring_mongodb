package com.bustation.mongodb.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bustation.mongodb.dto.MotoristaDTO;
import com.bustation.mongodb.model.Motorista;

@SpringBootTest
class MotoristaMapperTest {

    @Autowired
    private MotoristaMapper mapper;

    @Test
    void deveConverterEntityParaDTO() {
        // Arrange
        Motorista entity = new Motorista("1", "João da Silva", "12345678901", "D");

        // Act
        MotoristaDTO dto = mapper.toDTO(entity);

        // Assert
        assertNotNull(dto);
        assertEquals("1", dto.id());
        assertEquals("João da Silva", dto.nome());
        assertEquals("12345678901", dto.cnh());
        assertEquals("D", dto.categoria());
    }

    @Test
    void deveConverterDTOParaEntity() {
        // Arrange
        MotoristaDTO dto = new MotoristaDTO("2", "Maria Oliveira", "10987654321", "B");

        // Act
        Motorista entity = mapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals("2", entity.getId());
        assertEquals("Maria Oliveira", entity.getNome());
        assertEquals("10987654321", entity.getCnh());
        assertEquals("B", entity.getCategoria());
    }
}