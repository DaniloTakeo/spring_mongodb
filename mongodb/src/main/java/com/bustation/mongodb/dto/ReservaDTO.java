package com.bustation.mongodb.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservaDTO(

	    String id,

	    @NotBlank(message = "O ID do passageiro é obrigatório")
	    String idPassageiro,

	    @NotBlank(message = "O ID da viagem é obrigatório")
	    String idViagem,

	    @NotNull(message = "A data da reserva é obrigatória")
	    @Future(message = "A data da reserva deve ser futura")
	    LocalDateTime dataReserva,

	    @Min(value = 1, message = "O número do assento deve ser maior ou igual a 1")
	    int assento

	) {}