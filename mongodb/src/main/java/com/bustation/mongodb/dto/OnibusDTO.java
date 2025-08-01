package com.bustation.mongodb.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO que representa os dados de um ônibus.
 *
 * @param id         identificador único do ônibus
 * @param modelo     modelo do ônibus, com no mínimo 2
 * e no máximo 100 caracteres
 * @param placa      placa do ônibus no formato ABC-1234
 * @param capacidade número de assentos disponíveis, entre 10 e 100
 */
public record OnibusDTO(

    String id,

    @NotBlank(message = "O modelo é obrigatório")
    @Size(
        min = TAMANHO_MINIMO,
        max = TAMANHO_MAXIMO,
        message = "O modelo deve ter entre 2 e 100 caracteres"
    )
    String modelo,

    @NotBlank(message = "A placa é obrigatória")
    @Pattern(
        regexp = "^[A-Z]{3}-\\d{4}$",
        message = "A placa deve estar no formato ABC-1234"
    )
    String placa,

    @Min(value = CAPACIDADE_MINIMA,
        message = "A capacidade mínima é 10 lugares")
    @Max(value = CAPACIDADE_MAXIMA,
        message = "A capacidade máxima é 100 lugares")
    int capacidade

) {
    /**
     * Constante para defininr o tamanho mínimo de um modelo.
    */
    public static final int TAMANHO_MINIMO = 2;

    /**
     * Constante para definir o tamanho máximo de um modelo.
     */
    public static final int TAMANHO_MAXIMO = 100;

    /**
     * Constante para defininr a capapcidade mininma de um ônibus.
     */
    public static final int CAPACIDADE_MINIMA = 10;

    /**
     * Constante para definir a capapcidade máxima de um ônibus.
     */
    public static final int CAPACIDADE_MAXIMA = 100;
}
