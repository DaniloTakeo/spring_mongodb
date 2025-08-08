package com.bustation.mongodb.controller;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.bustation.mongodb.config.security.SecurityConfigTestProfile;
import com.bustation.mongodb.dto.ViagemDTO;
import com.bustation.mongodb.exception.GlobalExceptionHandler;
import com.bustation.mongodb.repository.UsuarioRepository;
import com.bustation.mongodb.service.AuthService;
import com.bustation.mongodb.service.JwtService;
import com.bustation.mongodb.service.ViagemService;

@Import(SecurityConfigTestProfile.class)
@SuppressWarnings("removal")
@WebMvcTest(controllers = {ViagemController.class, GlobalExceptionHandler.class})
@ActiveProfiles("test")
class ViagemControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private ViagemService service;
    
    @MockBean
    private JwtService jwtService;
    
    @MockBean
    private AuthService authService;
    
    @MockBean
    private UsuarioRepository usuarioRepository;

    private ViagemDTO viagemDTO;

    @BeforeEach
    void setUp() {
        viagemDTO = new ViagemDTO(
                "1",
                "São Paulo",
                "Rio de Janeiro",
                LocalDateTime.now().plusDays(1),
                "bus-001",
                "motorista-001"
        );
    }

    @Nested
    @DisplayName("GET /viagens")
    class ListarTodos {

        @Test
        @DisplayName("Deve retornar lista paginada de viagens com status 200")
        void deveListarViagens() throws Exception {
            Page<ViagemDTO> page = new PageImpl<>(List.of(viagemDTO));
            given(service.findAll(any(PageRequest.class))).willReturn(page);

            mockMvc.perform(get("/viagens")
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].origem", is("São Paulo")));

            verify(service).findAll(any(PageRequest.class));
        }
    }

    @Nested
    @DisplayName("GET /viagens/{id}")
    class BuscarPorId {

        @Test
        @DisplayName("Deve retornar viagem existente com status 200")
        void deveRetornarViagem() throws Exception {
            given(service.findById("1")).willReturn(viagemDTO);

            mockMvc.perform(get("/viagens/{id}", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.destino", is("Rio de Janeiro")));

            verify(service).findById("1");
        }
    }

    @Nested
    @DisplayName("POST /viagens")
    class Criar {

        @Test
        @DisplayName("Deve criar viagem e retornar 201 com Location header")
        void deveCriarViagem() throws Exception {
            given(service.save(any(ViagemDTO.class))).willReturn(viagemDTO);

            String json = """
                    {
                      "origem": "São Paulo",
                      "destino": "Rio de Janeiro",
                      "dataHora": "%s",
                      "idOnibus": "bus-001",
                      "idMotorista": "motorista-001"
                    }
                    """.formatted(viagemDTO.dataHora());

            mockMvc.perform(post("/viagens")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "http://localhost/viagens/" + viagemDTO.id()))
                    .andExpect(jsonPath("$.origem", is("São Paulo")));

            verify(service).save(any(ViagemDTO.class));
        }
    }

    @Nested
    @DisplayName("PUT /viagens/{id}")
    class Atualizar {

        @Test
        @DisplayName("Deve atualizar viagem existente e retornar 200")
        void deveAtualizarViagem() throws Exception {
            given(service.update(eq("1"), any(ViagemDTO.class))).willReturn(viagemDTO);

            String json = """
                    {
                      "origem": "São Paulo",
                      "destino": "Rio de Janeiro",
                      "dataHora": "%s",
                      "idOnibus": "bus-001",
                      "idMotorista": "motorista-001"
                    }
                    """.formatted(viagemDTO.dataHora());

            mockMvc.perform(put("/viagens/{id}", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.destino", is("Rio de Janeiro")));

            verify(service).update(eq("1"), any(ViagemDTO.class));
        }
    }

    @Nested
    @DisplayName("DELETE /viagens/{id}")
    class Deletar {

        @Test
        @DisplayName("Deve deletar viagem existente e retornar 204")
        void deveDeletarViagem() throws Exception {
            mockMvc.perform(delete("/viagens/{id}", "1"))
                    .andExpect(status().isNoContent());

            verify(service).delete("1");
        }
    }
}