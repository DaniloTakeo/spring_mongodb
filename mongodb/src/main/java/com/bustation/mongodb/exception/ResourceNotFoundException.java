package com.bustation.mongodb.exception;

public class ResourceNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7376743075590770175L;

	public ResourceNotFoundException(String mensagem) {
        super(mensagem);
    }
}