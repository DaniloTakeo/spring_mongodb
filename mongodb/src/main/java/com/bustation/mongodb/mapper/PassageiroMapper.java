package com.bustation.mongodb.mapper;

import org.mapstruct.Mapper;

import com.bustation.mongodb.dto.PassageiroDTO;
import com.bustation.mongodb.model.Passageiro;

/**
 * Interface responsável por mapear objetos entre {@code Passageiro} e
 * {@code PassageiroDTO}, utilizando o MapStruct.
 *
 * <p>O Spring injeta esta interface automaticamente.</p>
 */
@Mapper(componentModel = "spring")
public interface PassageiroMapper {

    /**
     * Converte uma entidade {@code Passageiro} em um {@code PassageiroDTO}.
     *
     * @param entity a entidade {@code Passageiro}
     * @return o objeto {@code PassageiroDTO} correspondente
     */
    PassageiroDTO toDTO(Passageiro entity);

    /**
     * Converte um {@code PassageiroDTO} em uma entidade {@code Passageiro}.
     *
     * @param dto o objeto {@code PassageiroDTO}
     * @return a entidade {@code Passageiro} correspondente
     */
    Passageiro toEntity(PassageiroDTO dto);
}
