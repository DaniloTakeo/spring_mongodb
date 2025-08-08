package com.bustation.mongodb.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bustation.mongodb.repository.UsuarioRepository;
import com.bustation.mongodb.service.AuthService;
import com.bustation.mongodb.service.JwtService;

import lombok.RequiredArgsConstructor;

/**
 * Classe de configuração da segurança da aplicação.
 *
 * <p>Define as regras de autorização, gerenciamento de sessão,
 * integração com o filtro JWT e configura os beans necessários
 * para autenticação e criptografia de senhas.
 *
 * <p>Utiliza autenticação baseada em token JWT e
 * desabilita o uso de sessões com estado.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile("default")
public class SecurityConfig {

    /**
     * Serviço responsável por validar e extrair informações dos tokens JWT.
     */
    private final JwtService jwtService;

    /**
     * Repositório para acesso aos dados de usuários.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Define a cadeia de filtros de segurança da aplicação.
     *
     * <p>Desabilita o CSRF, permite acesso público
     * a alguns endpoints (como login, Swagger e Actuator)
     * e exige autenticação para os demais.
     * Também configura o gerenciamento de sessão
     * como stateless e adiciona o filtro {@link JwtFilter}
     * antes do filtro de autenticação padrão.
     *
     * @param http o objeto de configuração HTTP fornecido pelo Spring Security
     * @return a cadeia de filtros de segurança configurada
     * @throws Exception se ocorrer erro na construção da cadeia
     */
    @Bean
    @SuppressWarnings("removal")
    public SecurityFilterChain filterChain(final HttpSecurity http)
            throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",
                                "/auth",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/actuator",
                                "/actuator/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy
                            .STATELESS))
                .addFilterBefore(new JwtFilter(jwtService,
                        usuarioRepository),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Define o {@link AuthenticationManager},
     * integrando o serviço de autenticação da aplicação
     * com o gerenciador do Spring Security.
     *
     * @param http        o objeto de configuração HTTP
     * @param authService serviço que implementa
     * {@link org.springframework.security.core.userdetails.UserDetailsService}
     * @return o gerenciador de autenticação configurado
     * @throws Exception se ocorrer erro na configuração
     */
    @Bean
    @SuppressWarnings("removal")
    public AuthenticationManager authenticationManager(
            final HttpSecurity http,
            final AuthService authService) throws Exception {

        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(authService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    /**
     * Define o {@link PasswordEncoder} utilizado para codificação de senhas.
     *
     * <p>Utiliza o algoritmo BCrypt.
     *
     * @return uma instância de {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
