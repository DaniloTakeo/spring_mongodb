package com.bustation.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bustation.mongodb.model.Reserva;

/**
 * Repositório para operações com a entidade Reserva.
 */
public interface ReservaRepository
    extends MongoRepository<Reserva, String> {

    /**
     * Busca reservas associadas ao ID da viagem.
     *
     * @param idViagem identificador da viagem
     * @return lista de reservas correspondentes
     */
    List<Reserva> findByIdViagem(String idViagem);

    /**
     * Busca reservas associadas ao ID do passageiro.
     *
     * @param idPassageiro identificador do passageiro
     * @return lista de reservas correspondentes
     */
    List<Reserva> findByIdPassageiro(String idPassageiro);
}
