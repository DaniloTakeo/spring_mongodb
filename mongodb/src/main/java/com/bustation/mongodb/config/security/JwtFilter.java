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
public class JwtFilter extends OncePerRequestFilter {

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
     * Posição do ínicio do token no cabeçalho Authorization.
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

    /**
     * Intercepta requisições HTTP para extrair e validar o token JWT.
     *
     * <p>Se o token estiver presente no cabeçalho Authorization e for válido,
     * o usuário correspondente será autenticado no contexto de segurança.
     *
     * @param request   a requisição HTTP recebida
     * @param response  a resposta HTTP que será enviada
     * @param chain     o filtro encadeado para continuar o processamento
     * da requisição
     * @throws ServletException se ocorrer erro durante
     * o processamento do filtro
     * @throws IOException      se ocorrer erro de entrada/saída
     */
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
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(TOKEN_POSITION);
            String login = jwtService.validarToken(token);

            usuarioRepository.findByLogin(login).ifPresent(user -> {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user.getLogin(),
                                null,
                                List
                                .of(new SimpleGrantedAuthority(user.getRole()))
                        );
                SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
            });
        }

        chain.doFilter(request, response);
    }
}
