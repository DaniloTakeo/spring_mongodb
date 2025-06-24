package com.bustation.mongodb.unit_tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bustation.mongodb.model.Usuario;
import com.bustation.mongodb.repository.UsuarioRepository;
import com.bustation.mongodb.service.AutenticacaoService;

class AutenticacaoServiceTest {

    private UsuarioRepository repository;
    private AutenticacaoService service;

    @BeforeEach
    void setUp() {
        repository = mock(UsuarioRepository.class);
        service = new AutenticacaoService(repository);
    }

    @Test
    void deveRetornarUsuarioQuandoUsuarioExistir() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setLogin("danilo");
        usuario.setSenha("senha123");
        usuario.setRole("ROLE_USER");

        when(repository.findByLogin("danilo"))
                .thenReturn(Optional.of(usuario));

        // Act
        UserDetails userDetails = service.loadUserByUsername("danilo");

        // Assert
        assertThat(userDetails.getUsername()).isEqualTo("danilo");
        assertThat(userDetails.getPassword()).isEqualTo("senha123");
        assertThat(userDetails.getAuthorities()).anySatisfy(
            auth -> assertThat(auth.getAuthority()).isEqualTo("ROLE_USER")
        );

        verify(repository, times(1)).findByLogin("danilo");
    }

    @Test
    void deveLancarExcecao_quandoUsuarioNaoExistir() {
        // Arrange
        when(repository.findByLogin("inexistente"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.loadUserByUsername("inexistente"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Usuário não encontrado");

        verify(repository, times(1)).findByLogin("inexistente");
    }
}