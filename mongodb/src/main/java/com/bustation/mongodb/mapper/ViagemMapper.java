package com.bustation.mongodb.mapper;

import org.mapstruct.Mapper;

import com.bustation.mongodb.dto.ViagemDTO;
import com.bustation.mongodb.model.Viagem;

/**
 * Interface responsável por mapear objetos entre {@code Viagem} e
 * {@code ViagemDTO}, utilizando o MapStruct.
 *
 * <p>O Spring injeta esta interface automaticamente.</p>
 */
@Mapper(componentModel = "spring")
public interface ViagemMapper {

    /**
     * Converte uma entidade {@code Viagem} em um {@code ViagemDTO}.
     *
     * @param entity a entidade {@code Viagem}
     * @return o objeto {@code ViagemDTO} correspondente
     */
    ViagemDTO toDTO(Viagem entity);

    /**
     * Converte um {@code ViagemDTO} em uma entidade {@code Viagem}.
     *
     * @param dto o objeto {@code ViagemDTO}
     * @return a entidade {@code Viagem} correspondente
     */
    Viagem toEntity(ViagemDTO dto);
}
