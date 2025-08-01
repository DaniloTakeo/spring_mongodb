package com.bustation.mongodb.mapper;

import org.mapstruct.Mapper;

import com.bustation.mongodb.dto.OnibusDTO;
import com.bustation.mongodb.model.Onibus;

/**
 * Interface responsável por mapear objetos entre {@code Onibus} e
 * {@code OnibusDTO}, utilizando o MapStruct.
 *
 * <p>O Spring injeta esta interface automaticamente.</p>
 */
@Mapper(componentModel = "spring")
public interface OnibusMapper {

    /**
     * Converte uma entidade {@code Onibus} em um {@code OnibusDTO}.
     *
     * @param onibus a entidade {@code Onibus}
     * @return o objeto {@code OnibusDTO} correspondente
     */
    OnibusDTO toDTO(Onibus onibus);

    /**
     * Converte um {@code OnibusDTO} em uma entidade {@code Onibus}.
     *
     * @param dto o objeto {@code OnibusDTO}
     * @return a entidade {@code Onibus} correspondente
     */
    Onibus toEntity(OnibusDTO dto);
}
