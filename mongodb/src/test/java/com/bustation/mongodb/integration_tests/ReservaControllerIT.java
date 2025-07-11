package com.bustation.mongodb.integration_tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.bustation.mongodb.dto.ReservaDTO;
import com.bustation.mongodb.model.Reserva;
import com.bustation.mongodb.repository.ReservaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WithMockUser(username = "admin", roles = {"ADMIN"})
@AutoConfigureMockMvc
class ReservaControllerIT extends BaseIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservaRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void deveCriarReserva() throws Exception {
        ReservaDTO dto = new ReservaDTO(
                null,
                "passageiro123",
                "viagem456",
                LocalDateTime.now().plusDays(1),
                10
        );

        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.idPassageiro").value("passageiro123"))
                .andExpect(jsonPath("$.idViagem").value("viagem456"))
                .andExpect(jsonPath("$.assento").value(10));
    }

    @Test
    void deveBuscarReservaPorId() throws Exception {
        Reserva reserva = new Reserva();
        reserva.setIdPassageiro("p1");
        reserva.setIdViagem("v1");
        reserva.setAssento(5);
        reserva.setDataReserva(LocalDateTime.now().plusDays(1));
        reserva = repository.save(reserva);

        mockMvc.perform(get("/reservas/" + reserva.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPassageiro").value("p1"))
                .andExpect(jsonPath("$.idViagem").value("v1"))
                .andExpect(jsonPath("$.assento").value(5));
    }

    @Test
    void deveRetornar404_QuandoBuscarReservaInexistente() throws Exception {
        mockMvc.perform(get("/reservas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarReservas() throws Exception {
        Reserva reserva = new Reserva();
        reserva.setIdPassageiro("p2");
        reserva.setIdViagem("v2");
        reserva.setAssento(12);
        reserva.setDataReserva(LocalDateTime.now().plusDays(2));
        repository.save(reserva);

        mockMvc.perform(get("/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].idPassageiro").value("p2"));
    }

    @Test
    void deveAtualizarReserva() throws Exception {
        Reserva reserva = new Reserva();
        reserva.setIdPassageiro("p3");
        reserva.setIdViagem("v3");
        reserva.setAssento(15);
        reserva.setDataReserva(LocalDateTime.now().plusDays(1));
        reserva = repository.save(reserva);

        ReservaDTO atualizado = new ReservaDTO(
                null,
                "p3",
                "v3",
                LocalDateTime.now().plusDays(2),
                20
        );

        mockMvc.perform(put("/reservas/" + reserva.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assento").value(20));
    }

    @Test
    void deveDeletarReserva() throws Exception {
        Reserva reserva = new Reserva();
        reserva.setIdPassageiro("p4");
        reserva.setIdViagem("v4");
        reserva.setAssento(22);
        reserva.setDataReserva(LocalDateTime.now().plusDays(1));
        reserva = repository.save(reserva);

        mockMvc.perform(delete("/reservas/" + reserva.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404_QuandoDeletarReservaInexistente() throws Exception {
        mockMvc.perform(delete("/reservas/999"))
                .andExpect(status().isNotFound());
    }
}