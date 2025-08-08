package com.bustation.mongodb.mapper;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.bustation.mongodb.dto.ReservaDTO;
import com.bustation.mongodb.model.Reserva;

@SpringBootTest
@ActiveProfiles("test")
class ReservaMapperTest {

    private final ReservaMapper mapper = Mappers.getMapper(ReservaMapper.class);

    @Test
    void deveConverterReservaParaDTO() {
        // Arrange
        Reserva reserva = new Reserva();
        reserva.setId("res123");
        reserva.setIdPassageiro("pass001");
        reserva.setIdViagem("via789");
        reserva.setDataReserva(LocalDateTime.of(2099, 12, 31, 23, 59));
        reserva.setAssento(12);

        // Act
        ReservaDTO dto = mapper.toDTO(reserva);

        // Assert
        assertNotNull(dto);
        assertEquals("res123", dto.id());
        assertEquals("pass001", dto.idPassageiro());
        assertEquals("via789", dto.idViagem());
        assertEquals(LocalDateTime.of(2099, 12, 31, 23, 59), dto.dataReserva());
        assertEquals(12, dto.assento());
    }

    @Test
    void deveConverterDTOPraReserva() {
        // Arrange
        LocalDateTime dataFutura = LocalDateTime.now().plusDays(5);
        ReservaDTO dto = new ReservaDTO(
            "res999",
            "pass999",
            "via999",
            dataFutura,
            1
        );

        // Act
        Reserva reserva = mapper.toEntity(dto);

        // Assert
        assertNotNull(reserva);
        assertEquals("res999", reserva.getId());
        assertEquals("pass999", reserva.getIdPassageiro());
        assertEquals("via999", reserva.getIdViagem());
        assertEquals(dataFutura, reserva.getDataReserva());
        assertEquals(1, reserva.getAssento());
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