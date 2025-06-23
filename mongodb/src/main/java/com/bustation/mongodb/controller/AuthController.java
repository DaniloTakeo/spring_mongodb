package com.bustation.mongodb.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bustation.mongodb.dto.LoginDTO;
import com.bustation.mongodb.dto.RegisterDTO;
import com.bustation.mongodb.model.Usuario;
import com.bustation.mongodb.repository.UsuarioRepository;
import com.bustation.mongodb.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints de login e registro de usuários")
public class AuthController {

	private final AuthService authService;
	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	@Operation(summary = "Realiza login e retorna o token JWT", description = "Recebe login e senha válidos, e retorna um token de autenticação JWT.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Token gerado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"token\": \"jwt_token_exemplo\"}"))),
			@ApiResponse(responseCode = "401", description = "Credenciais inválidas") })
	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO dto) {
		String token = authService.autenticar(dto.login(), dto.senha());
		return ResponseEntity.ok(Map.of("token", token));
	}

	@Operation(summary = "Cadastra um novo usuário", description = "Cria um novo usuário com login, senha e papel (role). Retorna erro se o login já existir.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
			@ApiResponse(responseCode = "409", description = "Usuário já existe") })
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO dto) {
		if (usuarioRepository.findByLogin(dto.login()).isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já existe.");
		}

		Usuario novoUsuario = new Usuario();
		novoUsuario.setLogin(dto.login());
		novoUsuario.setSenha(passwordEncoder.encode(dto.senha()));
		novoUsuario.setRole(dto.role() != null ? dto.role() : "ROLE_USER");

		usuarioRepository.save(novoUsuario);
		return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso.");
	}
}