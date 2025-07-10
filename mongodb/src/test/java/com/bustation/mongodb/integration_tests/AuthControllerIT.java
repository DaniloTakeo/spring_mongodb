package com.bustation.mongodb.integration_tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.bustation.mongodb.model.Usuario;
import com.bustation.mongodb.repository.UsuarioRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
    }

    @Test
    @Order(1)
    void deveRegistrarUsuarioComSucesso() throws Exception {
        String json = """
                {
                    "login": "usuario_teste",
                    "senha": "senha123",
                    "role": "ROLE_USER"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string("Usuário cadastrado com sucesso."));
    }

    @Test
    @Order(2)
    void deveRetornarConflitoAoRegistrarUsuarioDuplicado() throws Exception {
        Usuario user = new Usuario(null, "usuario_teste", passwordEncoder.encode("senha123"), "ROLE_USER");
        usuarioRepository.save(user);

        String json = """
                {
                    "login": "usuario_teste",
                    "senha": "senha123",
                    "role": "ROLE_USER"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(content().string("Usuário já existe."));
    }

    @Test
    @Order(3)
    void deveAutenticarUsuarioComSucessoERetornarToken() throws Exception {
        Usuario user = new Usuario(null, "usuario_login", passwordEncoder.encode("senha123"), "ROLE_USER");
        usuarioRepository.save(user);

        String json = """
                {
                    "login": "usuario_login",
                    "senha": "senha123"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @Order(4)
    void deveRetornarErroAoAutenticarComSenhaIncorreta() throws Exception {
        Usuario user = new Usuario(null, "usuario_erro", passwordEncoder.encode("senha123"), "ROLE_USER");
        usuarioRepository.save(user);

        String json = """
                {
                    "login": "usuario_erro",
                    "senha": "errada"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }
}