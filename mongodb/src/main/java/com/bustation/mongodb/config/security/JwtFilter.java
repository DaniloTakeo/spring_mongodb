package com.bustation.mongodb.config.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bustation.mongodb.repository.UsuarioRepository;
import com.bustation.mongodb.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Filtro que intercepta requisições HTTP para validar o token
 * JWT presente no cabeçalho Authorization.
 *
 * <p>Se o token for válido, a autenticação é estabelecida
 * no contexto de segurança do Spring,
 * permitindo que a requisição prossiga como uma requisição autenticada.
 *
 * <p>Esse filtro é executado apenas uma vez
 * por requisição {@link OncePerRequestFilter}.
 */
@Slf4j
public final class JwtFilter extends OncePerRequestFilter {

    /**
     * Serviço responsável pela validação do token JWT.
     */
    private final JwtService jwtService;

    /**
     * Repositório para acesso aos dados de usuários,
     * utilizado para buscar informações de autenticação.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Posição do início do token no cabeçalho Authorization.
     */
    private static final int TOKEN_POSITION = 7;

    /**
     * Construtor que injeta as dependências necessárias
     * para a validação do JWT.
     *
     * @param pJwtService          o serviço de validação de tokens JWT
     * @param pUsuarioRepository   o repositório de usuários
     */
    public JwtFilter(final JwtService pJwtService,
                     final UsuarioRepository pUsuarioRepository) {
        this.jwtService = pJwtService;
        this.usuarioRepository = pUsuarioRepository;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain chain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if (path != null && (path.startsWith("/auth")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/actuator"))) {
            log.debug("Ignorando filtro JWT para o path: {}", path);
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            log.debug("Cabeçalho Authorization presente. "
                    + "Iniciando validação do token.");

            try {
                String token = authHeader.substring(TOKEN_POSITION);
                String login = jwtService.validarToken(token);

                usuarioRepository.findByLogin(login)
                .ifPresentOrElse(user -> {
                    UsernamePasswordAuthenticationToken
                        authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user.getLogin(),
                                    null,
                                    List.of(new SimpleGrantedAuthority(user
                                            .getRole()))
                            );
                    SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
                    log.info("Usuário autenticado com sucesso. Login: {}", user
                            .getLogin());
                }, () -> {
                    log.warn("Usuário não encontrado para login "
                            + "extraído do token.");
                });

            } catch (Exception e) {
                log.error("Erro durante a validação do token JWT", e);
            }
        } else {
            log.debug("Cabeçalho Authorization ausente ou inválido.");
        }

        chain.doFilter(request, response);
    }
}
