package com.bustation.mongodb.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO que representa uma viagem de ônibus cadastrada no sistema.
 *
 * @param id          identificador da viagem
 * @param origem      cidade de origem
 * @param destino     cidade de destino
 * @param dataHora    data e hora programadas da viagem (deve ser futura)
 * @param idOnibus    identificador do ônibus responsável pela viagem
 * @param idMotorista identificador do motorista que irá conduzir
 */
public record ViagemDTO(

    String id,

    @NotBlank(message = "A origem não pode estar em branco")
    String origem,

    @NotBlank(message = "O destino não pode estar em branco")
    String destino,

    @NotNull(message = "A data e hora da viagem são obrigatórias")
    @Future(
        message = "A data e hora da viagem devem estar no futuro"
    )
    LocalDateTime dataHora,

    @NotBlank(message = "O ID do ônibus é obrigatório")
    String idOnibus,

    @NotBlank(message = "O ID do motorista é obrigatório")
    String idMotorista

) { }
