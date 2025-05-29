package com.bustation.mongodb.mapper;

import org.mapstruct.Mapper;

import com.bustation.mongodb.dto.ViagemDTO;
import com.bustation.mongodb.model.Viagem;

@Mapper(componentModel = "spring")
public interface ViagemMapper {

    ViagemDTO toDTO(Viagem entity);
    Viagem toEntity(ViagemDTO dto);
}