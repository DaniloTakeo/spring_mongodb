package com.bustation.mongodb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO que representa os dados de um motorista.
 *
 * @param id        identificador único do motorista
 * @param nome      nome completo do motorista, com tamanho entre 2 e 100
 *                  caracteres
 * @param cnh       número da CNH do motorista, contendo exatamente 11 dígitos
 * @param categoria categoria da CNH, deve ser uma das letras: A, B, C, D ou E
 */
public record MotoristaDTO(

    String id,

    @NotBlank(message = "O nome é obrigatório")
    @Size(
        min = TAMANHO_MINIMO,
        max = TAMANHO_MAXIMO,
        message = "O nome deve ter entre 2 e 100 caracteres"
    )
    String nome,

    @NotBlank(message = "A CNH é obrigatória")
    @Pattern(
        regexp = "\\d{11}",
        message = "A CNH deve conter exatamente 11 dígitos"
    )
    String cnh,

    @NotBlank(message = "A categoria da CNH é obrigatória")
    @Pattern(
        regexp = "^[ABCDE]$",
        message = "A categoria da CNH deve ser uma das "
        		+ "seguintes: A, B, C, D ou E"
    )
    String categoria

) {
    /**
     * Constante para defininr o tamanho mínimo de um nome.
     */
    public static final int TAMANHO_MINIMO = 2;

    /**
     * Constante para definir o tamanho máximo de um nome.
     */
    public static final int TAMANHO_MAXIMO = 100;
}
