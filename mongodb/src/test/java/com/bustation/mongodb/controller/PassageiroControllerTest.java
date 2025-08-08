package com.bustation.mongodb.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.bustation.mongodb.config.security.SecurityConfigTestProfile;
import com.bustation.mongodb.dto.PassageiroDTO;
import com.bustation.mongodb.exception.GlobalExceptionHandler;
import com.bustation.mongodb.exception.ResourceNotFoundException;
import com.bustation.mongodb.repository.UsuarioRepository;
import com.bustation.mongodb.service.AuthService;
import com.bustation.mongodb.service.JwtService;
import com.bustation.mongodb.service.PassageiroService;

@Import(SecurityConfigTestProfile.class)
@SuppressWarnings("removal")
@WebMvcTest(controllers = {PassageiroController.class, GlobalExceptionHandler.class})
@ActiveProfiles("test")
class PassageiroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassageiroService service;

    private PassageiroDTO passageiroDTO;
    
    @MockBean
    private JwtService jwtService;
    
    @MockBean
    private AuthService authService;
    
    @MockBean
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        passageiroDTO = new PassageiroDTO(
                "1",
                "Maria Souza",
                "12345678901",
                "maria@email.com"
        );
    }

    @Test
    @DisplayName("Deve listar passageiros com paginação")
    void deveListarPassageiros() throws Exception {
        Page<PassageiroDTO> page = new PageImpl<>(List.of(passageiroDTO));
        when(service.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/passageiros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Maria Souza"));
    }

    @Test
    @DisplayName("Deve buscar passageiro por ID")
    void deveBuscarPorId() throws Exception {
        when(service.findById("1")).thenReturn(passageiroDTO);

        mockMvc.perform(get("/passageiros/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("12345678901"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar passageiro inexistente")
    void deveRetornar404AoBuscarInexistente() throws Exception {
        when(service.findById("99")).thenThrow(new ResourceNotFoundException("Passageiro não encontrado"));

        mockMvc.perform(get("/passageiros/{id}", "99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Passageiro não encontrado"));
    }

    @Test
    @DisplayName("Deve criar passageiro e retornar 201 com Location header")
    void deveCriarPassageiro() throws Exception {
        when(service.save(any(PassageiroDTO.class))).thenReturn(passageiroDTO);

        String json = """
            {
              "nome": "Maria Souza",
              "cpf": "12345678901",
              "email": "maria@email.com"
            }
            """;

        mockMvc.perform(post("/passageiros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/passageiros/" + passageiroDTO.id()))
                .andExpect(jsonPath("$.email").value("maria@email.com"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao criar passageiro inválido")
    void deveRetornar400AoCriarInvalido() throws Exception {
        String json = """
            {
              "nome": "",
              "cpf": "111",
              "email": "email_invalido"
            }
            """;

        mockMvc.perform(post("/passageiros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve atualizar passageiro existente")
    void deveAtualizarPassageiro() throws Exception {
        when(service.update(eq("1"), any(PassageiroDTO.class))).thenReturn(passageiroDTO);

        String json = """
            {
              "nome": "Maria Souza",
              "cpf": "12345678901",
              "email": "maria@email.com"
            }
            """;

        mockMvc.perform(put("/passageiros/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria Souza"));
    }

    @Test
    @DisplayName("Deve deletar passageiro existente")
    void deveDeletarPassageiro() throws Exception {
        mockMvc.perform(delete("/passageiros/{id}", "1"))
                .andExpect(status().isNoContent());

        verify(service).delete("1");
    }
}