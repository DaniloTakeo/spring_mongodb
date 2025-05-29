package com.bustation.mongodb.mapper;

import org.mapstruct.Mapper;

import com.bustation.mongodb.dto.PassageiroDTO;
import com.bustation.mongodb.model.Passageiro;

@Mapper(componentModel = "spring")
public interface PassageiroMapper {

    PassageiroDTO toDTO(Passageiro entity);
    Passageiro toEntity(PassageiroDTO dto);
}