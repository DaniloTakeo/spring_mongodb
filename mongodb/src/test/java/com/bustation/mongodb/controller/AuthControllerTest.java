package com.bustation.mongodb.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.bustation.mongodb.config.security.SecurityConfig;
import com.bustation.mongodb.dto.LoginDTO;
import com.bustation.mongodb.dto.RegisterDTO;
import com.bustation.mongodb.model.Usuario;
import com.bustation.mongodb.repository.UsuarioRepository;
import com.bustation.mongodb.service.AuthService;
import com.bustation.mongodb.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Import(SecurityConfig.class)
@WebMvcTest(AuthController.class)
@SuppressWarnings("removal")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

	@MockBean
    private AuthService authService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;
    
    @MockBean
    private JwtService jwtService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void deveAutenticarUsuarioERetornarToken() throws Exception {
        // Arrange
        LoginDTO loginDTO = new LoginDTO("usuario", "senha");
        String tokenSimulado = "jwt_token_simulado";

        when(authService.autenticar("usuario", "senha"))
            .thenReturn(tokenSimulado);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value(tokenSimulado));
    }

    @Test
    void deveCadastrarUsuarioComSucesso() throws Exception {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO("novoUser", "senhaSegura", "ROLE_ADMIN");

        when(usuarioRepository.findByLogin("novoUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("senhaSegura")).thenReturn("senhaCriptografada");

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)))
            .andExpect(status().isCreated())
            .andExpect(content().string("Usuário cadastrado com sucesso."));
    }

    @Test
    void deveRetornarConflictQuandoUsuarioJaExiste() throws Exception {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO("usuarioExistente", "senha", "ROLE_USER");
        Usuario usuario = new Usuario();
        usuario.setLogin("usuarioExistente");

        when(usuarioRepository.findByLogin("usuarioExistente"))
            .thenReturn(Optional.of(usuario));

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)))
            .andExpect(status().isConflict())
            .andExpect(content().string("Usuário já existe."));
    }
}