package com.java.exception.apierror;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiErrorSlim {

    @JsonIgnore
    private HttpStatus status;

    private String message;

    public ApiErrorSlim(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiErrorSlim(String message) {
        this.message = message;
    }
}
