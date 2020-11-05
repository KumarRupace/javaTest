package com.dbstest.allclasses;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * @author Henry
 */
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(AppException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String processError(AppException appEx) {
        return appEx.getMessage();
    }
}
