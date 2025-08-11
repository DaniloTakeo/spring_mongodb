package com.bustation.mongodb.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.bustation.mongodb.config.security.SecurityConfigTestProfile;
import com.bustation.mongodb.dto.MotoristaDTO;
import com.bustation.mongodb.exception.GlobalExceptionHandler;
import com.bustation.mongodb.exception.ResourceNotFoundException;
import com.bustation.mongodb.repository.UsuarioRepository;
import com.bustation.mongodb.service.AuthService;
import com.bustation.mongodb.service.JwtService;
import com.bustation.mongodb.service.MotoristaService;

@Import(SecurityConfigTestProfile.class)
@WebMvcTest(controllers = { MotoristaController.class, GlobalExceptionHandler.class })
@SuppressWarnings("removal")
@ActiveProfiles("test")
class MotoristaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MotoristaService service;

	@MockBean
	private JwtService jwtService;

	@MockBean
	private AuthService authService;

	@MockBean
	private UsuarioRepository usuarioRepository;

	private final MotoristaDTO motoristaDTO = new MotoristaDTO(UUID.randomUUID().toString(), "João da Silva",
			"12345678900", "B");

	@Nested
	@DisplayName("GET /motoristas")
	class ListarTodos {

		@Test
		@DisplayName("Deve retornar lista paginada de motoristas com status 200")
		void deveListarTodosComPaginacao() throws Exception {
			Page<MotoristaDTO> page = new PageImpl<>(List.of(motoristaDTO));
			when(service.findAll(any(Pageable.class))).thenReturn(page);

			mockMvc.perform(
					get("/motoristas").param("page", "0").param("size", "10").accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andExpect(jsonPath("$.content[0].nome").value("João da Silva"));

			verify(service).findAll(any(Pageable.class));
		}
	}

	@Nested
	@DisplayName("GET /motoristas/{id}")
	class BuscarPorId {

		@Test
        @DisplayName("Deve retornar motorista existente com status 200")
        void deveBuscarMotoristaPorId() throws Exception {
            when(service.findById(anyString())).thenReturn(motoristaDTO);

            mockMvc.perform(get("/motoristas/{id}", motoristaDTO.id())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome").value("João da Silva"));

            verify(service).findById(motoristaDTO.id());
        }

		@Test
        @DisplayName("Deve retornar 404 quando motorista não encontrado")
        void deveRetornar404SeNaoEncontrado() throws Exception {
            when(service.findById(anyString()))
                .thenThrow(new ResourceNotFoundException("Motorista não encontrado"));

            mockMvc.perform(get("/motoristas/{id}", UUID.randomUUID())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("Not Found"))
                    .andExpect(jsonPath("$.message").value("Motorista não encontrado"));

            verify(service).findById(anyString());
        }
	}

	@Nested
	@DisplayName("POST /motoristas")
	class Criar {

		@Test
        @DisplayName("Deve criar motorista e retornar 201 com location header")
        void deveCriarMotorista() throws Exception {
            when(service.save(any(MotoristaDTO.class))).thenReturn(motoristaDTO);

            String json = """
            	    {
            	      "id": "%s",
            	      "nome": "João da Silva",
            	      "cnh": "12345678900",
            	      "categoria": "B"
            	    }
            	    """.formatted(motoristaDTO.id());

            mockMvc.perform(post("/motoristas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location",
                            "http://localhost/motoristas/" + motoristaDTO.id()))
                    .andExpect(jsonPath("$.nome").value("João da Silva"));

            verify(service).save(any(MotoristaDTO.class));
        }

		@Nested
		@DisplayName("PUT /motoristas/{id}")
		class Atualizar {

		@Test
    	@DisplayName("Deve atualizar motorista existente e retornar 200")
    	void deveAtualizarMotorista() throws Exception {
    	    when(service.update(anyString(), any(MotoristaDTO.class)))
    	            .thenReturn(motoristaDTO);

    	    String json = """
    	        {
    	          "id": "%s",
    	          "nome": "João da Silva",
    	          "cnh": "12345678900",
    	          "categoria": "B"
    	        }
    	        """.formatted(motoristaDTO.id());

    	    mockMvc.perform(put("/motoristas/{id}", motoristaDTO.id())
    	                    .contentType(MediaType.APPLICATION_JSON)
    	                    .content(json))
    	            .andExpect(status().isOk())
    	            .andExpect(jsonPath("$.nome").value("João da Silva"));

    	    verify(service).update(eq(motoristaDTO.id()), any(MotoristaDTO.class));
    		}
		}

		@Nested
		@DisplayName("DELETE /motoristas/{id}")
		class Deletar {

			@Test
			@DisplayName("Deve deletar motorista existente e retornar 204")
			void deveDeletarMotorista() throws Exception {
				doNothing().when(service).delete(anyString());

				mockMvc.perform(delete("/motoristas/{id}", motoristaDTO.id())).andExpect(status().isNoContent());

				verify(service).delete(motoristaDTO.id());
			}
		}
	}
}
