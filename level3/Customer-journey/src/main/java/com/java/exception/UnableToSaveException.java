package com.java.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UnableToSaveException extends RuntimeException {
    public UnableToSaveException(String errorMessage){
        super(String.format("Unable to save the object. Error: %s", errorMessage));
    }
}