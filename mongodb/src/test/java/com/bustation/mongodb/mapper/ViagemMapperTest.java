package com.bustation.mongodb.mapper;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.bustation.mongodb.dto.ViagemDTO;
import com.bustation.mongodb.model.Viagem;

public class ViagemMapperTest {

    private final ViagemMapper mapper = Mappers.getMapper(ViagemMapper.class);

    @Test
    void deveConverterViagemParaDTO() {
        // Arrange
        Viagem viagem = new Viagem();
        viagem.setId("v001");
        viagem.setOrigem("São Paulo");
        viagem.setDestino("Rio de Janeiro");
        viagem.setDataHora(LocalDateTime.of(2099, 1, 1, 10, 0));
        viagem.setIdOnibus("bus123");
        viagem.setIdMotorista("mot456");

        // Act
        ViagemDTO dto = mapper.toDTO(viagem);

        // Assert
        assertNotNull(dto);
        assertEquals("v001", dto.id());
        assertEquals("São Paulo", dto.origem());
        assertEquals("Rio de Janeiro", dto.destino());
        assertEquals(LocalDateTime.of(2099, 1, 1, 10, 0), dto.dataHora());
        assertEquals("bus123", dto.idOnibus());
        assertEquals("mot456", dto.idMotorista());
    }

    @Test
    void deveConverterDTOPraViagem() {
        // Arrange
        LocalDateTime futura = LocalDateTime.now().plusDays(7);
        ViagemDTO dto = new ViagemDTO(
            "v999",
            "Curitiba",
            "Florianópolis",
            futura,
            "bus999",
            "mot999"
        );

        // Act
        Viagem viagem = mapper.toEntity(dto);

        // Assert
        assertNotNull(viagem);
        assertEquals("v999", viagem.getId());
        assertEquals("Curitiba", viagem.getOrigem());
        assertEquals("Florianópolis", viagem.getDestino());
        assertEquals(futura, viagem.getDataHora());
        assertEquals("bus999", viagem.getIdOnibus());
        assertEquals("mot999", viagem.getIdMotorista());
    }

    @Test
    void deveRetornarNullAoConverterViagemNulaParaDTO() {
        assertNull(mapper.toDTO(null));
    }

    @Test
    void deveRetornarNullAoConverterDTONuloParaViagem() {
        assertNull(mapper.toEntity(null));
    }
}