package com.bustation.mongodb.dto;

/**
 * Record que representa os dados de autenticação de um usuário.
 *
 * @param login identificador do usuário (e.g. e-mail ou nome de usuário)
 * @param senha a senha correspondente ao login
 */
public record LoginDTO(String login, String senha) {

    /**
     * Retorna o login do usuário.
     *
     * @return o identificador de login do usuário
     */
    public String login() {
        return login;
    }

    /**
     * Retorna a senha do usuário.
     *
     * @return a senha do usuário
     */
    public String senha() {
        return senha;
    }
}
