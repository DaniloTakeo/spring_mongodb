package com.bustation.mongodb.mapper;

import org.mapstruct.Mapper;

import com.bustation.mongodb.dto.ReservaDTO;
import com.bustation.mongodb.model.Reserva;

/**
 * Interface responsável por mapear objetos entre {@code Reserva} e
 * {@code ReservaDTO}, utilizando o MapStruct.
 *
 * <p>O Spring injeta esta interface automaticamente.</p>
 */
@Mapper(componentModel = "spring")
public interface ReservaMapper {

    /**
     * Converte uma entidade {@code Reserva} em um {@code ReservaDTO}.
     *
     * @param reserva a entidade {@code Reserva}
     * @return o objeto {@code ReservaDTO} correspondente
     */
    ReservaDTO toDTO(Reserva reserva);

    /**
     * Converte um {@code ReservaDTO} em uma entidade {@code Reserva}.
     *
     * @param dto o objeto {@code ReservaDTO}
     * @return a entidade {@code Reserva} correspondente
     */
    Reserva toEntity(ReservaDTO dto);
}
