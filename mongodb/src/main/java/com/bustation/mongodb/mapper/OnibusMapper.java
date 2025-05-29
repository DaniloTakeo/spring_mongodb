package com.bustation.mongodb.mapper;

import org.mapstruct.Mapper;

import com.bustation.mongodb.dto.OnibusDTO;
import com.bustation.mongodb.model.Onibus;

@Mapper(componentModel = "spring")
public interface OnibusMapper {

    OnibusDTO toDTO(Onibus onibus);
    Onibus toEntity(OnibusDTO dto);
}