package com.jse.proyectoretotecnico.exception;

import java.util.Map;

public class BadRequestException extends RuntimeException {

    private Map<String, String> fieldErrors;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
