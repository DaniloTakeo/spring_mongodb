package com.bustation.mongodb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MotoristaDTO(

	    String id,

	    @NotBlank(message = "O nome é obrigatório")
	    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
	    String nome,

	    @NotBlank(message = "A CNH é obrigatória")
	    @Pattern(regexp = "\\d{11}", message = "A CNH deve conter exatamente 11 dígitos")
	    String cnh,

	    @NotBlank(message = "A categoria da CNH é obrigatória")
	    @Pattern(regexp = "^(A|B|C|D|E)$", message = "A categoria da CNH deve ser uma das seguintes: A, B, C, D ou E")
	    String categoria

	) {}