package com.ynov.adventures.exception;

public class AventurierBusinessException extends RuntimeException {

    public AventurierBusinessException(String message) {
        super(message);
    }

    public AventurierBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
