package com.bustation.mongodb.integration_tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.bustation.mongodb.dto.OnibusDTO;
import com.bustation.mongodb.model.Onibus;
import com.bustation.mongodb.repository.OnibusRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WithMockUser(username = "admin", roles = {"ADMIN"})
@AutoConfigureMockMvc
class OnibusControllerIT extends BaseIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OnibusRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void deveCriarOnibus() throws Exception {
        OnibusDTO dto = new OnibusDTO(null, "Volvo B270F", "ABC-1234", 42);

        mockMvc.perform(post("/onibus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.modelo").value("Volvo B270F"))
                .andExpect(jsonPath("$.placa").value("ABC-1234"))
                .andExpect(jsonPath("$.capacidade").value(42));
    }

    @Test
    void deveListarOnibus() throws Exception {
        repository.save(new Onibus(null, "DEF-5678", "Mercedes O500", 50));

        mockMvc.perform(get("/onibus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].modelo").value("Mercedes O500"));
    }

    @Test
    void deveBuscarOnibusPorId() throws Exception {
        Onibus salvo = repository.save(new Onibus(null, "GHI-9876", "Marcopolo Torino", 44));

        mockMvc.perform(get("/onibus/" + salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelo").value("Marcopolo Torino"));
    }

    @Test
    void deveAtualizarOnibus() throws Exception {
        Onibus salvo = repository.save(new Onibus(null, "Comil Campione", "JKL-4321", 40));
        OnibusDTO atualizado = new OnibusDTO(null, "Comil Versatile", "JKL-4321", 45);

        mockMvc.perform(put("/onibus/" + salvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelo").value("Comil Versatile"))
                .andExpect(jsonPath("$.capacidade").value(45));
    }

    @Test
    void deveDeletarOnibus() throws Exception {
        Onibus salvo = repository.save(new Onibus(null, "Neobus Spectrum", "MNO-3210", 36));

        mockMvc.perform(delete("/onibus/" + salvo.getId()))
                .andExpect(status().isNoContent());
    }
}

