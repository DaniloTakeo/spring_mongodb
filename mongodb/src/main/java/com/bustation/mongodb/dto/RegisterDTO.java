package com.bustation.mongodb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado no registro de novos usuários.
 *
 * @param login login do usuário, entre 3 e 50 caracteres
 * @param senha senha do usuário, com no mínimo 6 caracteres
 * @param role  papel do usuário (ex: ADMIN, USER)
 */
public record RegisterDTO(

    @NotBlank(message = "O login é obrigatório")
    @Size(
        min = LOGIN_TAMANHO_MINIMO,
        max = LOGIN_TAMANHO_MAXIMO,
        message = "O login deve ter entre 3 e 50 caracteres"
    )
    String login,

    @NotBlank(message = "A senha é obrigatória")
    @Size(
        min = SENHA_TAMANHO_MINIMO,
        message = "A senha deve ter no mínimo 6 caracteres"
    )
    String senha,

    @NotBlank(message = "A role é obrigatória")
    String role

) {
    /**
     * Constante para defininr o tamanho mínimo de um login.
     */
    public static final int LOGIN_TAMANHO_MINIMO = 3;

    /**
     * Constante para definir o tamanho máximo de um login.
     */
    public static final int LOGIN_TAMANHO_MAXIMO = 50;

    /**
     * Constante para defininr o tamanho mínimo de um login.
     */
    public static final int SENHA_TAMANHO_MINIMO = 3;
}
