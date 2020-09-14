package com.java.exception;

public class IllegalCustomerArgumentException extends RuntimeException {
    public IllegalCustomerArgumentException(String message) {
        super(message);
    }
}
