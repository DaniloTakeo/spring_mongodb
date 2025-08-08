package com.bustation.mongodb.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
import com.bustation.mongodb.dto.OnibusDTO;
import com.bustation.mongodb.exception.GlobalExceptionHandler;
import com.bustation.mongodb.repository.UsuarioRepository;
import com.bustation.mongodb.service.AuthService;
import com.bustation.mongodb.service.JwtService;
import com.bustation.mongodb.service.OnibusService;

@Import(SecurityConfigTestProfile.class)
@SuppressWarnings("removal")
@WebMvcTest(controllers = {OnibusController.class, GlobalExceptionHandler.class})
@ActiveProfiles("test")
class OnibusControllerTest {

    @Autowired
    MockMvc mockMvc;

	@MockBean
    OnibusService service;
	
    @MockBean
    private JwtService jwtService;
    
    @MockBean
    private AuthService authService;
    
    @MockBean
    private UsuarioRepository usuarioRepository;

    OnibusDTO onibusDTO = new OnibusDTO(
            UUID.randomUUID().toString(),
            "Marcopolo Paradiso",
            "ABC-1234",
            50
    );

    @Nested
    @DisplayName("GET /onibus")
    class ListarTodos {

        @Test
        @DisplayName("Deve retornar lista paginada de ônibus com status 200")
        void deveListarTodos() throws Exception {
            when(service.findAll(any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(onibusDTO)));

            mockMvc.perform(get("/onibus"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].modelo").value("Marcopolo Paradiso"));

            verify(service).findAll(any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("GET /onibus/{id}")
    class BuscarPorId {

        @Test
        @DisplayName("Deve retornar ônibus por ID com status 200")
        void deveBuscarPorId() throws Exception {
            when(service.findById(onibusDTO.id())).thenReturn(onibusDTO);

            mockMvc.perform(get("/onibus/{id}", onibusDTO.id()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.placa").value("ABC-1234"));

            verify(service).findById(onibusDTO.id());
        }
    }

    @Nested
    @DisplayName("POST /onibus")
    class Criar {

        @Test
        @DisplayName("Deve criar ônibus e retornar 201 com Location header")
        void deveCriarOnibus() throws Exception {
            when(service.save(any(OnibusDTO.class))).thenReturn(onibusDTO);

            String json = """
                {
                  "modelo": "Marcopolo Paradiso",
                  "placa": "ABC-1234",
                  "capacidade": 50
                }
                """;

            mockMvc.perform(post("/onibus")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location",
                            "http://localhost/onibus/" + onibusDTO.id()))
                    .andExpect(jsonPath("$.modelo").value("Marcopolo Paradiso"));

            verify(service).save(any(OnibusDTO.class));
        }
    }

    @Nested
    @DisplayName("PUT /onibus/{id}")
    class Atualizar {

        @Test
        @DisplayName("Deve atualizar ônibus e retornar 200")
        void deveAtualizarOnibus() throws Exception {
            when(service.update(eq(onibusDTO.id()), any(OnibusDTO.class)))
                    .thenReturn(onibusDTO);

            String json = """
                {
                  "modelo": "Marcopolo Paradiso",
                  "placa": "ABC-1234",
                  "capacidade": 50
                }
                """;

            mockMvc.perform(put("/onibus/{id}", onibusDTO.id())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.capacidade").value(50));

            verify(service).update(eq(onibusDTO.id()), any(OnibusDTO.class));
        }
    }

    @Nested
    @DisplayName("DELETE /onibus/{id}")
    class Deletar {

        @Test
        @DisplayName("Deve deletar ônibus e retornar 204")
        void deveDeletarOnibus() throws Exception {
            doNothing().when(service).delete(onibusDTO.id());

            mockMvc.perform(delete("/onibus/{id}", onibusDTO.id()))
                    .andExpect(status().isNoContent());

            verify(service).delete(onibusDTO.id());
        }
    }
}