package com.bustation.mongodb.model;

import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa um usuário do sistema com credenciais de acesso.
 *
 * Implementa a interface {@link UserDetails} para integração com o Spring
 * Security.
 */
@Document(collection = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    private static final long serialVersionUID =
        9049484444103484756L;

    /**
     * Identificador único do usuário.
     */
    @Id
    private String id;

    /**
     * Login do usuário.
     */
    private String login;

    /**
     * Senha criptografada do usuário.
     */
    private String senha;

    /**
     * Papel (role) do usuário no sistema.
     */
    private String role;

    /**
     * Retorna as autoridades do usuário com base em seu papel.
     *
     * @return lista de {@link GrantedAuthority}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role));
    }

    /**
     * Retorna a senha do usuário.
     *
     * @return senha criptografada
     */
    @Override
    public String getPassword() {
        return this.senha;
    }

    /**
     * Retorna o nome de usuário (login).
     *
     * @return login
     */
    @Override
    public String getUsername() {
        return this.login;
    }
}
