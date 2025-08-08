package com.bustation.mongodb.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.bustation.mongodb.model.Usuario;
import com.bustation.mongodb.service.JwtService;

import io.jsonwebtoken.JwtException;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
    "api.security.token.secret=qAnFM6YvZ8kH+1OovvSgkn8WGbS4eE+4WTv7K/79pYE=",
    "jwt.expiration=3600000"
})
class JwtServiceTest {

    @Autowired
    private Environment env;

    private JwtService jwtService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secret", env.getProperty("api.security.token.secret"));
        ReflectionTestUtils.setField(jwtService, "expirationMillis", Long.valueOf(env.getProperty("jwt.expiration")));

        usuario = new Usuario();
        usuario.setLogin("danilo");
    }

    @Test
    void gerarTokenDeveRetornarTokenValido() {
        String token = jwtService.gerarToken(usuario);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void validarTokenDeveRetornarLoginQuandoTokenForValido() {
        String token = jwtService.gerarToken(usuario);

        String subject = jwtService.validarToken(token);

        assertEquals("danilo", subject);
    }

    @Test
    void validarTokenDeveLancarExceptionQuandoTokenForInvalido() {
        assertThrows(JwtException.class, () -> {
            jwtService.validarToken("token-invalido");
        });
    }
}