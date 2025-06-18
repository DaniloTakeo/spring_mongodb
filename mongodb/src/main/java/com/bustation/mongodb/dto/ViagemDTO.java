package com.bustation.mongodb.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ViagemDTO(
	    String id,

	    @NotBlank(message = "A origem não pode estar em branco")
	    String origem,

	    @NotBlank(message = "O destino não pode estar em branco")
	    String destino,

	    @NotNull(message = "A data e hora da viagem são obrigatórias")
	    @Future(message = "A data e hora da viagem devem estar no futuro")
	    LocalDateTime dataHora,

	    @NotBlank(message = "O ID do ônibus é obrigatório")
	    String idOnibus,

	    @NotBlank(message = "O ID do motorista é obrigatório")
	    String idMotorista
	) {}