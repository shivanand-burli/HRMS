package com.hrms.app.config;

public class ValidationError extends RuntimeException {
    public ValidationError(String message) {
        super(message);
    }
}
