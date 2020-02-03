package com.java.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerAlreadyFoundException extends RuntimeException{
    public CustomerAlreadyFoundException(String message) {
        super(message);
    }
}
