package com.bustation.mongodb.integration_tests;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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

import com.bustation.mongodb.model.Motorista;
import com.bustation.mongodb.repository.MotoristaRepository;

@AutoConfigureMockMvc
class MotoristaControllerIT extends BaseIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MotoristaRepository repository;

    private Motorista motorista;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        motorista = repository.save(new Motorista(null, "João da Silva", "12345678901", "D"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveListarMotoristasComSucesso() throws Exception {
        mockMvc.perform(get("/motoristas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].nome", is("João da Silva")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveBuscarMotoristaPorIdComSucesso() throws Exception {
        mockMvc.perform(get("/motoristas/" + motorista.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("João da Silva")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveCriarMotoristaComDadosValidos() throws Exception {
        String body = """
            {
                "nome": "Maria Souza",
                "cnh": "98765432100",
                "categoria": "B"
            }
        """;

        mockMvc.perform(post("/motoristas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is("Maria Souza")))
                .andExpect(jsonPath("$.cnh", is("98765432100")))
                .andExpect(jsonPath("$.categoria", is("B")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveAtualizarMotoristaComIdExistente() throws Exception {
        String body = """
            {
                "nome": "João Atualizado",
                "cnh": "12345678901",
                "categoria": "C"
            }
        """;

        mockMvc.perform(put("/motoristas/" + motorista.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("João Atualizado")))
                .andExpect(jsonPath("$.categoria", is("C")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveExcluirMotoristaComIdExistente() throws Exception {
        mockMvc.perform(delete("/motoristas/" + motorista.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornar404QuandoBuscarMotoristaInexistente() throws Exception {
        mockMvc.perform(get("/motoristas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornar400QuandoCriarMotoristaComDadosInvalidos() throws Exception {
        String body = """
            {
                "nome": "",
                "cnh": "123",
                "categoria": "Z"
            }
        """;

        mockMvc.perform(post("/motoristas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
