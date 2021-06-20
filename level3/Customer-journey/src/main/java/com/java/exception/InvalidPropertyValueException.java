package com.java.exception;

public class InvalidPropertyValueException extends RuntimeException {
    public InvalidPropertyValueException(String message) {
        super(message);
    }
}
