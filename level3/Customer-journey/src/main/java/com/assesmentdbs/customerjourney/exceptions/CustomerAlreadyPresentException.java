package com.assesmentdbs.customerjourney.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Prithvi Panchapakeshan
 * An exception thrown when user is already present in the repository.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerAlreadyPresentException extends RuntimeException {

    public CustomerAlreadyPresentException(String message) {
        super(message);
    }
}
