package com.bustation.mongodb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PassageiroDTO(

	    String id,

	    @NotBlank(message = "O nome é obrigatório")
	    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
	    String nome,

	    @NotBlank(message = "O CPF é obrigatório")
	    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos")
	    String cpf,

	    @NotBlank(message = "O e-mail é obrigatório")
	    @Email(message = "O e-mail deve ser válido")
	    String email

	) {}