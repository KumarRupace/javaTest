package com.assesmentdbs.customerjourney.exceptions;

import lombok.Data;

import java.util.Date;

/**
 * @author Prithvi Panchapakeshan
 */
@Data
public class ExceptionResponse {
    private Date timestamp;
    private String message;
    private String details;

    public ExceptionResponse(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }
}
