package com.bustation.mongodb.exception;

/**
 * Exceção personalizada lançada quando um recurso solicitado não é encontrado
 * na aplicação. Representa um erro do tipo 404 (Not Found).
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Identificador de versão da classe para fins de serialização.
     */
    private static final long serialVersionUID = 7376743075590770175L;

    /**
     * Cria uma nova instância da exceção com a mensagem fornecida.
     *
     * @param mensagem a mensagem descritiva do erro
     */
    public ResourceNotFoundException(final String mensagem) {
        super(mensagem);
    }
}
