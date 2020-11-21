package com.java.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UnableToDeleteException extends RuntimeException {
    public UnableToDeleteException(String errorMessage) {
        super(String.format("Unable to delete the object. Error: %s", errorMessage));
    }
}