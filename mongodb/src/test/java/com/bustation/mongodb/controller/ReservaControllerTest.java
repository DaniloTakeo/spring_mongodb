package com.bustation.mongodb.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.bustation.mongodb.config.security.SecurityConfigTestProfile;
import com.bustation.mongodb.dto.ReservaDTO;
import com.bustation.mongodb.exception.GlobalExceptionHandler;
import com.bustation.mongodb.repository.UsuarioRepository;
import com.bustation.mongodb.service.AuthService;
import com.bustation.mongodb.service.JwtService;
import com.bustation.mongodb.service.ReservaService;

@Import(SecurityConfigTestProfile.class)
@SuppressWarnings("removal")
@WebMvcTest(controllers = {ReservaController.class, GlobalExceptionHandler.class})
@ActiveProfiles("test")
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService service;
    
    @MockBean
    private JwtService jwtService;
    
    @MockBean
    private AuthService authService;
    
    @MockBean
    private UsuarioRepository usuarioRepository;

    private final ReservaDTO reservaDTO = new ReservaDTO(
            "1",
            "pass1",
            "viagem1",
            LocalDateTime.now().plusDays(1),
            5
    );

    @Nested
    @DisplayName("GET /reservas")
    class ListarTodas {
        @Test
        @DisplayName("Deve retornar lista paginada de reservas")
        void deveListarTodas() throws Exception {
            Mockito.when(service.findAll(any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(reservaDTO)));

            mockMvc.perform(get("/reservas"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].id", is("1")))
                    .andExpect(jsonPath("$.content[0].assento", is(5)));

            Mockito.verify(service).findAll(any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("GET /reservas/{id}")
    class BuscarPorId {
        @Test
        @DisplayName("Deve retornar reserva quando existir")
        void deveRetornarReserva() throws Exception {
            Mockito.when(service.findById("1")).thenReturn(reservaDTO);

            mockMvc.perform(get("/reservas/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is("1")));

            Mockito.verify(service).findById("1");
        }

        @Test
        @DisplayName("Deve retornar 404 quando reserva não existir")
        void deveRetornarNotFound() throws Exception {
            Mockito.when(service.findById("1"))
                    .thenThrow(new RuntimeException("Não encontrada"));

            mockMvc.perform(get("/reservas/1"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /reservas")
    class Criar {
        @Test
        @DisplayName("Deve criar reserva e retornar 201")
        void deveCriarReserva() throws Exception {
            Mockito.when(service.save(any(ReservaDTO.class)))
                    .thenReturn(reservaDTO);

            String json = """
                {
                  "idPassageiro": "pass1",
                  "idViagem": "viagem1",
                  "dataReserva": "%s",
                  "assento": 5
                }
                """.formatted(reservaDTO.dataReserva());

            mockMvc.perform(post("/reservas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location",
                            "http://localhost/reservas/1"))
                    .andExpect(jsonPath("$.id", is("1")));

            Mockito.verify(service).save(any(ReservaDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 400 quando dados inválidos")
        void deveRetornarBadRequest() throws Exception {
            String json = """
                {
                  "idPassageiro": "",
                  "idViagem": "viagem1",
                  "dataReserva": "%s",
                  "assento": 0
                }
                """.formatted(reservaDTO.dataReserva());

            mockMvc.perform(post("/reservas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("PUT /reservas/{id}")
    class Atualizar {
        @Test
        @DisplayName("Deve atualizar reserva e retornar 200")
        void deveAtualizarReserva() throws Exception {
            Mockito.when(service.update(eq("1"), any(ReservaDTO.class)))
                    .thenReturn(reservaDTO);

            String json = """
                {
                  "idPassageiro": "pass1",
                  "idViagem": "viagem1",
                  "dataReserva": "%s",
                  "assento": 5
                }
                """.formatted(reservaDTO.dataReserva());

            mockMvc.perform(put("/reservas/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is("1")));
        }
    }

    @Nested
    @DisplayName("DELETE /reservas/{id}")
    class Deletar {
        @Test
        @DisplayName("Deve deletar reserva e retornar 204")
        void deveDeletarReserva() throws Exception {
            mockMvc.perform(delete("/reservas/1"))
                    .andExpect(status().isNoContent());

            Mockito.verify(service).delete("1");
        }

        @Test
        @DisplayName("Deve retornar 404 quando reserva não existir")
        void deveRetornarNotFound() throws Exception {
            Mockito.doThrow(new RuntimeException("Não encontrada"))
                    .when(service).delete("1");

            mockMvc.perform(delete("/reservas/1"))
                    .andExpect(status().isNotFound());
        }
    }
}