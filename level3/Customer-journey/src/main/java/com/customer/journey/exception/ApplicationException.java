package com.customer.journey.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Jhonson
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * @param message
     */
    public ApplicationException(String message){
        super(message);
    }
}