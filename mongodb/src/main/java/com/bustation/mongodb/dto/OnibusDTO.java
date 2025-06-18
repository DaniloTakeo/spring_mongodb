package com.bustation.mongodb.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OnibusDTO(

	    String id,

	    @NotBlank(message = "O modelo é obrigatório")
	    @Size(min = 2, max = 100, message = "O modelo deve ter entre 2 e 100 caracteres")
	    String modelo,

	    @NotBlank(message = "A placa é obrigatória")
	    @Pattern(regexp = "^[A-Z]{3}-\\d{4}$", message = "A placa deve estar no formato ABC-1234")
	    String placa,

	    @Min(value = 10, message = "A capacidade mínima é 10 lugares")
	    @Max(value = 100, message = "A capacidade máxima é 100 lugares")
	    int capacidade

	) {}