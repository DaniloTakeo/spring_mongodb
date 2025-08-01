package com.bustation.mongodb.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO utilizado para representar uma reserva de assento em uma viagem.
 *
 * @param id            identificador da reserva
 * @param idPassageiro  identificador do passageiro
 * @param idViagem      identificador da viagem
 * @param dataReserva   data e hora da reserva (deve ser futura)
 * @param assento       número do assento reservado (mínimo 1)
 */
public record ReservaDTO(

    String id,

    @NotBlank(message = "O ID do passageiro é obrigatório")
    String idPassageiro,

    @NotBlank(message = "O ID da viagem é obrigatório")
    String idViagem,

    @NotNull(message = "A data da reserva é obrigatória")
    @Future(message = "A data da reserva deve ser futura")
    LocalDateTime dataReserva,

    @Min(
        value = ASSENTO_MINIMO,
        message = "O número do assento deve ser maior ou igual a 1"
    )
    int assento

) {
    /**
     * Constante para defininr o número mínimo de assentos.
     */
    public static final int ASSENTO_MINIMO = 1;
}
