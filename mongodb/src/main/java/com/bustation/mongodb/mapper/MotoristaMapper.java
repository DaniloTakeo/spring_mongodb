package com.bustation.mongodb.mapper;

import org.mapstruct.Mapper;

import com.bustation.mongodb.dto.MotoristaDTO;
import com.bustation.mongodb.model.Motorista;

/**
 * Interface responsável por mapear objetos entre a entidade {@code Motorista}
 * e o DTO {@code MotoristaDTO}, utilizando o MapStruct.
 *
 * <p>Este mapper facilita a conversão entre objetos de domínio e objetos
 * de transferência de dados, promovendo uma separação clara entre camadas.</p>
 *
 * <p>A anotação {@code @Mapper(componentModel = "spring")} permite que o
 * Spring gerencie a injeção de dependência desta interface.</p>
 */
@Mapper(componentModel = "spring")
public interface MotoristaMapper {

    /**
     * Converte uma entidade {@code Motorista}
     * para um DTO {@code MotoristaDTO}.
     *
     * @param entity a entidade do tipo {@code Motorista}
     * @return o DTO correspondente do tipo {@code MotoristaDTO}
     */
    MotoristaDTO toDTO(Motorista entity);

    /**
     * Converte um DTO {@code MotoristaDTO}
     * para uma entidade {@code Motorista}.
     *
     * @param dto o DTO do tipo {@code MotoristaDTO}
     * @return a entidade correspondente do tipo {@code Motorista}
     */
    Motorista toEntity(MotoristaDTO dto);
}
