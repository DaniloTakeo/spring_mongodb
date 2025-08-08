package com.bustation.mongodb.mapper;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.bustation.mongodb.dto.OnibusDTO;
import com.bustation.mongodb.model.Onibus;

@SpringBootTest
@ActiveProfiles("test")
class OnibusMapperTest {

    @Autowired
    private OnibusMapper mapper;

    @Test
    void deveConverterEntityParaDTO() {
        // Arrange
        Onibus onibus = new Onibus("1", "ABC-1234", "Mercedes", 50);

        // Act
        OnibusDTO dto = mapper.toDTO(onibus);

        // Assert
        assertNotNull(dto);
        assertEquals("1", dto.id());
        assertEquals("Mercedes", dto.modelo());
        assertEquals("ABC-1234", dto.placa());
        assertEquals(50, dto.capacidade());
    }

    @Test
    void deveConverterDTOParaEntity() {
        // Arrange
        OnibusDTO dto = new OnibusDTO("2", "Volvo", "XYZ-5678", 42);

        // Act
        Onibus onibus = mapper.toEntity(dto);

        // Assert
        assertNotNull(onibus);
        assertEquals("2", onibus.getId());
        assertEquals("Volvo", onibus.getModelo());
        assertEquals("XYZ-5678", onibus.getPlaca());
        assertEquals(42, onibus.getCapacidade());
    }
    
    @Test
    void deveRetornarNullAoConverterReservaNulaParaDTO() {
        assertNull(mapper.toDTO(null));
    }

    @Test
    void deveRetornarNullAoConverterDTONuloParaReserva() {
        assertNull(mapper.toEntity(null));
    }
}
