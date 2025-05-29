package com.bustation.mongodb.dto;

import java.time.LocalDateTime;

public record ViagemDTO(
	    String id,
	    String origem,
	    String destino,
	    LocalDateTime dataHora,
	    String idOnibus,
	    String idMotorista
	) {}