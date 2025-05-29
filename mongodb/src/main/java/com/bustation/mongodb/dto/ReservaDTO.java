package com.bustation.mongodb.dto;

import java.time.LocalDateTime;

public record ReservaDTO(
	    String id,
	    String idPassageiro,
	    String idViagem,
	    LocalDateTime dataReserva,
	    int assento
	) {}