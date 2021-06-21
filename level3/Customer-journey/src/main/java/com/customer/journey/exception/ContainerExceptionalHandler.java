package com.customer.journey.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.customer.journey.util.CustomerConstants;

@ControllerAdvice
public class ContainerExceptionalHandler {

	@Autowired
	MessageSource messageSource;
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApplicationExceptionDetail> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request) {
		ApplicationExceptionDetail customerException 
					= new ApplicationExceptionDetail(ex.getMessage(), 
				messageSource.getMessage(CustomerConstants.ERR_MSG_GIVE_VALID_RESOURCE, null, request.getLocale()),
				   ((ServletWebRequest)request).getRequest().getRequestURI().toString());
		return new ResponseEntity<ApplicationExceptionDetail>(customerException, HttpStatus.NOT_FOUND);
    }
    
}
