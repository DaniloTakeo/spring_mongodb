package com.bustation.mongodb.mapper;

import org.mapstruct.Mapper;

import com.bustation.mongodb.dto.MotoristaDTO;
import com.bustation.mongodb.model.Motorista;

@Mapper(componentModel = "spring")
public interface MotoristaMapper {

    MotoristaDTO toDTO(Motorista entity);
    Motorista toEntity(MotoristaDTO dto);
}