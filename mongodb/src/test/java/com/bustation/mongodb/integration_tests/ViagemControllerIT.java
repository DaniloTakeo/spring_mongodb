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

import com.bustation.mongodb.dto.ViagemDTO;
import com.bustation.mongodb.model.Viagem;
import com.bustation.mongodb.repository.ViagemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WithMockUser(username = "admin", roles = {"ADMIN"})
@AutoConfigureMockMvc
class ViagemControllerIT extends BaseIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ViagemRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void deveCriarViagem() throws Exception {
        ViagemDTO dto = new ViagemDTO(
                null,
                "São Paulo",
                "Rio de Janeiro",
                LocalDateTime.now().plusDays(2),
                "onibus123",
                "motorista123"
        );

        mockMvc.perform(post("/viagens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.origem").value("São Paulo"))
                .andExpect(jsonPath("$.destino").value("Rio de Janeiro"))
                .andExpect(jsonPath("$.idOnibus").value("onibus123"))
                .andExpect(jsonPath("$.idMotorista").value("motorista123"));
    }

    @Test
    void deveBuscarViagemPorId() throws Exception {
        Viagem viagem = new Viagem();
        viagem.setOrigem("Belo Horizonte");
        viagem.setDestino("Curitiba");
        viagem.setDataHora(LocalDateTime.now().plusDays(3));
        viagem.setIdOnibus("bus789");
        viagem.setIdMotorista("driver456");
        viagem = repository.save(viagem);

        mockMvc.perform(get("/viagens/" + viagem.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.origem").value("Belo Horizonte"))
                .andExpect(jsonPath("$.destino").value("Curitiba"))
                .andExpect(jsonPath("$.idOnibus").value("bus789"))
                .andExpect(jsonPath("$.idMotorista").value("driver456"));
    }

    @Test
    void deveRetornar404QuandoBuscarViagemInexistente() throws Exception {
        mockMvc.perform(get("/viagens/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarViagens() throws Exception {
        Viagem viagem = new Viagem();
        viagem.setOrigem("Campinas");
        viagem.setDestino("Porto Alegre");
        viagem.setDataHora(LocalDateTime.now().plusDays(1));
        viagem.setIdOnibus("bus999");
        viagem.setIdMotorista("motor999");
        repository.save(viagem);

        mockMvc.perform(get("/viagens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].origem").value("Campinas"));
    }

    @Test
    void deveAtualizarViagem() throws Exception {
        Viagem viagem = new Viagem();
        viagem.setOrigem("Sorocaba");
        viagem.setDestino("Natal");
        viagem.setDataHora(LocalDateTime.now().plusDays(1));
        viagem.setIdOnibus("busA");
        viagem.setIdMotorista("driverA");
        viagem = repository.save(viagem);

        ViagemDTO atualizado = new ViagemDTO(
                null,
                "Sorocaba",
                "Recife",
                LocalDateTime.now().plusDays(2),
                "busA",
                "driverA"
        );

        mockMvc.perform(put("/viagens/" + viagem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destino").value("Recife"));
    }

    @Test
    void deveDeletarViagem() throws Exception {
        Viagem viagem = new Viagem();
        viagem.setOrigem("Florianópolis");
        viagem.setDestino("Joinville");
        viagem.setDataHora(LocalDateTime.now().plusDays(3));
        viagem.setIdOnibus("busX");
        viagem.setIdMotorista("driverX");
        viagem = repository.save(viagem);

        mockMvc.perform(delete("/viagens/" + viagem.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404_QuandoDeletarViagemInexistente() throws Exception {
        mockMvc.perform(delete("/viagens/999"))
                .andExpect(status().isNotFound());
    }
}