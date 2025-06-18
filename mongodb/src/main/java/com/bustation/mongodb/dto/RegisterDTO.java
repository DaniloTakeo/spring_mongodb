package com.bustation.mongodb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDTO(

	    @NotBlank(message = "O login é obrigatório")
	    @Size(min = 3, max = 50, message = "O login deve ter entre 3 e 50 caracteres")
	    String login,

	    @NotBlank(message = "A senha é obrigatória")
	    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
	    String senha,

	    @NotBlank(message = "A role é obrigatória")
	    String role

	) {}