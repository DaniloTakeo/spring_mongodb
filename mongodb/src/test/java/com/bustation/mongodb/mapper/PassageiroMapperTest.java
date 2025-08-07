package com.bustation.mongodb.mapper;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.bustation.mongodb.dto.PassageiroDTO;
import com.bustation.mongodb.model.Passageiro;

public class PassageiroMapperTest {

    private final PassageiroMapper mapper = Mappers.getMapper(PassageiroMapper.class);

    @Test
    void deveConverterPassageiroParaDTO() {
        // Arrange
        Passageiro passageiro = new Passageiro("123", "Carlos Silva", "12345678901", "carlos@email.com");

        // Act
        PassageiroDTO dto = mapper.toDTO(passageiro);

        // Assert
        assertNotNull(dto);
        assertEquals("123", dto.id());
        assertEquals("Carlos Silva", dto.nome());
        assertEquals("12345678901", dto.cpf());
        assertEquals("carlos@email.com", dto.email());
    }

    @Test
    void deveConverterDTOPraPassageiro() {
        // Arrange
        PassageiroDTO dto = new PassageiroDTO("456", "Ana Souza", "98765432100", "ana@email.com");

        // Act
        Passageiro entity = mapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals("456", entity.getId());
        assertEquals("Ana Souza", entity.getNome());
        assertEquals("98765432100", entity.getCpf());
        assertEquals("ana@email.com", entity.getEmail());
    }

    @Test
    void deveRetornarNullAoConverterDTO_Nulo() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void deveRetornarNullAoConverterEntity_Nulo() {
        assertNull(mapper.toDTO(null));
    }
}