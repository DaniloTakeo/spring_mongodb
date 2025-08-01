package com.bustation.mongodb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO que representa os dados de um passageiro.
 *
 * @param id    identificador único do passageiro
 * @param nome  nome do passageiro, entre 3 e 100 caracteres
 * @param cpf   CPF do passageiro, exatamente 11 dígitos numéricos
 * @param email e-mail válido do passageiro
 */
public record PassageiroDTO(

    String id,

    @NotBlank(message = "O nome é obrigatório")
    @Size(
        min = TAMANHO_MINIMO,
        max = TAMANHO_MAXIMO,
        message = "O nome deve ter entre 3 e 100 caracteres"
    )
    String nome,

    @NotBlank(message = "O CPF é obrigatório")
    @Pattern(
        regexp = "\\d{11}",
        message = "O CPF deve conter exatamente 11 dígitos numéricos"
    )
    String cpf,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O e-mail deve ser válido")
    String email

) {
    /**
     * Constante para defininr o tamanho mínimo de um nome.
     */
    public static final int TAMANHO_MINIMO = 3;

    /**
     * Constante para definir o tamanho máximo de um nome.
     */
    public static final int TAMANHO_MAXIMO = 100;
}
