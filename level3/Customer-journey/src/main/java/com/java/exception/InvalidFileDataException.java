package com.java.exception;

public class InvalidFileDataException extends RuntimeException {
    public InvalidFileDataException(String message) {
        super(message);
    }
}
