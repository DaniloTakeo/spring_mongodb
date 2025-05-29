package com.bustation.mongodb.mapper;

import org.mapstruct.Mapper;

import com.bustation.mongodb.dto.ReservaDTO;
import com.bustation.mongodb.model.Reserva;

@Mapper(componentModel = "spring")
public interface ReservaMapper {

    ReservaDTO toDTO(Reserva reserva);
    Reserva toEntity(ReservaDTO dto);
}