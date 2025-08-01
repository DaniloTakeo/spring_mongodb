package com.bustation.mongodb.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bustation.mongodb.model.Usuario;

/**
 * Repositório para operações com a entidade Usuario.
 */
public interface UsuarioRepository
    extends MongoRepository<Usuario, String> {

    /**
     * Busca um usuário pelo login.
     *
     * @param login nome de login do usuário
     * @return usuário correspondente, se encontrado
     */
    Optional<Usuario> findByLogin(String login);
}
