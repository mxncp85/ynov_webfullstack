package com.ynov.adventures.exception;

public class AventurierNotFoundException extends RuntimeException {

    public AventurierNotFoundException(String message) {
        super(message);
    }

    public AventurierNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
