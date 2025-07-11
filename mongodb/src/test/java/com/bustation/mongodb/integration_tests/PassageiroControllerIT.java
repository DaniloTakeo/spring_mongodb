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

import com.bustation.mongodb.dto.PassageiroDTO;
import com.bustation.mongodb.model.Passageiro;
import com.bustation.mongodb.repository.PassageiroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WithMockUser(username = "admin", roles = {"ADMIN"})
@AutoConfigureMockMvc
class PassageiroControllerIT extends BaseIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PassageiroRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void deveCriarPassageiro() throws Exception {
        PassageiroDTO dto = new PassageiroDTO(null, "Carlos Silva", "12345678901", "carlos@email.com");

        mockMvc.perform(post("/passageiros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Carlos Silva"))
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.email").value("carlos@email.com"));
    }

    @Test
    void deveListarPassageiros() throws Exception {
        repository.save(new Passageiro(null, "Maria Souza", "11122233344", "maria@email.com"));

        mockMvc.perform(get("/passageiros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].nome").value("Maria Souza"));
    }

    @Test
    void deveBuscarPassageiroPorId() throws Exception {
        Passageiro salvo = repository.save(new Passageiro(null, "Ana Paula", "22233344455", "ana@email.com"));

        mockMvc.perform(get("/passageiros/" + salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana Paula"))
                .andExpect(jsonPath("$.cpf").value("22233344455"));
    }

    @Test
    void deveAtualizarPassageiro() throws Exception {
        Passageiro salvo = repository.save(new Passageiro(null, "Bruno Lopes", "33344455566", "bruno@email.com"));
        PassageiroDTO atualizado = new PassageiroDTO(null, "Bruno Lima", "33344455566", "bruno.lima@email.com");

        mockMvc.perform(put("/passageiros/" + salvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Bruno Lima"))
                .andExpect(jsonPath("$.email").value("bruno.lima@email.com"));
    }

    @Test
    void deveDeletarPassageiro() throws Exception {
        Passageiro salvo = repository.save(new Passageiro(null, "Laura Melo", "44455566677", "laura@email.com"));

        mockMvc.perform(delete("/passageiros/" + salvo.getId()))
                .andExpect(status().isNoContent());
    }
}