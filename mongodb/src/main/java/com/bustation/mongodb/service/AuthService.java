package com.bustation.mongodb.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bustation.mongodb.model.Usuario;
import com.bustation.mongodb.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável pela autenticação de usuários e integração com o
 * Spring Security.
 * <p>
 * Implementa a interface {@link
 * org.springframework.security.core.userdetails.UserDetailsService} para
 * permitir a autenticação baseada em login via Spring Security.
 */
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    /**
     * Repositório para acesso a dados dos usuários.
     */
    private final UsuarioRepository repository;

    /**
     * Codificador de senha para validar credenciais.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Serviço para geração e validação de tokens JWT.
     */
    private final JwtService jwtService;

    /**
     * Autentica um usuário com base no login e senha fornecidos.
     *
     * @param login o login do usuário
     * @param senha a senha do usuário
     * @return um token JWT válido para o usuário autenticado
     * @throws UsernameNotFoundException se o usuário não for encontrado
     * @throws BadCredentialsException se a senha estiver incorreta
     */
    public String autenticar(final String login, final String senha) {
        Usuario user = repository.findByLogin(login)
            .orElseThrow(
                () -> new UsernameNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senha, user.getSenha())) {
            throw new BadCredentialsException("Senha inválida");
        }

        return jwtService.gerarToken(user);
    }

    /**
     * Carrega um usuário pelo nome de login para fins de autenticação.
     *
     * @param username o nome de login do usuário
     * @return os detalhes do usuário
     * @throws UsernameNotFoundException se o usuário não for encontrado
     */
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
        return repository.findByLogin(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}
