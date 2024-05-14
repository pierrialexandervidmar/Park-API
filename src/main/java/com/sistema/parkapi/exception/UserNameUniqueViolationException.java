package com.sistema.parkapi.exception;

public class UserNameUniqueViolationException extends RuntimeException {
    public UserNameUniqueViolationException(String message) {
        super(message);
    }
}
