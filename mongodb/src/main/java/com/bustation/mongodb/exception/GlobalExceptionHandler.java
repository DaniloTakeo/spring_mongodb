package com.bustation.mongodb.exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Classe responsável por capturar e tratar exceções lançadas na aplicação,
 * fornecendo respostas HTTP padronizadas e informativas aos clientes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Constante que define o HTTP Status Code para recursos não encontrados.
     */
    private static final int STATUS_CODE_NOT_FOUND = 404;

    /**
     * Trata a exceção {@link ResourceNotFoundException}, retornando uma
     * resposta HTTP 404 (Not Found) com detalhes sobre o erro ocorrido.
     *
     * @param ex a exceção lançada quando um recurso não é encontrado
     * @return um {@link ResponseEntity} com o corpo contendo informações
     *         sobre o erro
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            final ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", STATUS_CODE_NOT_FOUND,
                        "error", "Not Found",
                        "message", ex.getMessage()
                )
        );
    }

    /**
     * Trata a exceção {@link BadCredentialsException}, retornando uma
     * resposta HTTP 401 (Unauthorized) com uma mensagem genérica.
     *
     * @param ex a exceção lançada em caso de falha na autenticação
     * @return um {@link ResponseEntity} com uma mensagem de erro
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleCredenciaisInvalidas(
            final BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Credenciais inválidas");
    }
}
