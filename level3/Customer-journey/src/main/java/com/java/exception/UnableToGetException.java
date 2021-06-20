package com.java.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UnableToGetException extends RuntimeException {
    public UnableToGetException(String errorMessage){
        super(String.format("Unable to get the object. Error: %s", errorMessage));
    }
}
