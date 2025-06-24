package com.bustation.mongodb.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bustation.mongodb.model.Usuario;
import com.bustation.mongodb.repository.UsuarioRepository;
import com.bustation.mongodb.service.AuthService;
import com.bustation.mongodb.service.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private Usuario mockUsuario;

    @BeforeEach
    void setUp() {
        mockUsuario = new Usuario();
        mockUsuario.setId("1");
        mockUsuario.setLogin("danilo");
        mockUsuario.setSenha("senhaCriptografada");
        mockUsuario.setRole("ROLE_USER");
    }

    @Test
    void autenticarDeveRetornarTokenQuandoCredenciaisForemValidas() {
        when(repository.findByLogin("danilo")).thenReturn(Optional.of(mockUsuario));
        when(passwordEncoder.matches("123", "senhaCriptografada")).thenReturn(true);
        when(jwtService.gerarToken(mockUsuario)).thenReturn("fake-token");

        String token = authService.autenticar("danilo", "123");

        assertEquals("fake-token", token);
        verify(jwtService).gerarToken(mockUsuario);
    }

    @Test
    void autenticarDeveLancarExceptionQuandoUsuarioNaoExistir() {
        when(repository.findByLogin("inexistente")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
            () -> authService.autenticar("inexistente", "123"));
    }

    @Test
    void autenticarDeveLancarExceptionQuandoSenhaEstiverIncorreta() {
        when(repository.findByLogin("danilo")).thenReturn(Optional.of(mockUsuario));
        when(passwordEncoder.matches("senhaErrada", "senhaCriptografada")).thenReturn(false);

        assertThrows(BadCredentialsException.class,
            () -> authService.autenticar("danilo", "senhaErrada"));
    }

    @Test
    void loadUserByUsernameDeveRetornarUsuarioQuandoLoginForValido() {
        when(repository.findByLogin("danilo")).thenReturn(Optional.of(mockUsuario));

        UserDetails userDetails = authService.loadUserByUsername("danilo");

        assertNotNull(userDetails);
        assertEquals("danilo", userDetails.getUsername());
    }

    @Test
    void loadUserByUsernameDeveLancarExceptionQuandoLoginNaoExistir() {
        when(repository.findByLogin("desconhecido")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
            () -> authService.loadUserByUsername("desconhecido"));
    }
}