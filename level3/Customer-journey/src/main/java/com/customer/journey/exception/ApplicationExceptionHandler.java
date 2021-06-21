package com.customer.journey.exception;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import com.customer.journey.util.CustomerConstants;

/**
 * @author Jhonson
 *
 */
@ControllerAdvice
public class ApplicationExceptionHandler {

	@Autowired
	MessageSource messageSource;

	String errMsgDetail = CustomerConstants.ERR_MSG_GIVE_VALID_DATA;

	/**
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApplicationExceptionDetail> validationFromValidator(MethodArgumentNotValidException ex,
			WebRequest request) {

		List<String> errors = ex.getBindingResult().getAllErrors().stream()
				.map(x -> messageSource.getMessage(x.getCode(), null, request.getLocale()))
				.collect(Collectors.toList());

		ApplicationExceptionDetail customerException = new ApplicationExceptionDetail(errors.toString(),
				messageSource.getMessage(errMsgDetail, null, request.getLocale()),
				((ServletWebRequest) request).getRequest().getRequestURI().toString());

		return new ResponseEntity<ApplicationExceptionDetail>(customerException, HttpStatus.BAD_REQUEST);
	}

	/**
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApplicationExceptionDetail> validationFromConstraint(ConstraintViolationException ex,
			WebRequest request) {
		ApplicationExceptionDetail customerException = new ApplicationExceptionDetail(
				ex.getConstraintViolations().stream().findFirst().get().getMessage(),
				messageSource.getMessage(errMsgDetail, null, request.getLocale()),
				((ServletWebRequest) request).getRequest().getRequestURI().toString());
		return new ResponseEntity<ApplicationExceptionDetail>(customerException, HttpStatus.BAD_REQUEST);
	}

	/**
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ApplicationExceptionDetail> businessException(ApplicationException ex, WebRequest request) {
		ApplicationExceptionDetail customerException = new ApplicationExceptionDetail(
				messageSource.getMessage(ex.getMessage(), null, request.getLocale()),
				messageSource.getMessage(errMsgDetail, null, request.getLocale()),
				((ServletWebRequest) request).getRequest().getRequestURI().toString());
		return new ResponseEntity<ApplicationExceptionDetail>(customerException, HttpStatus.NOT_FOUND);
	}

	/**
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApplicationExceptionDetail> applicationGlobalException(Exception ex, WebRequest request) {
		ApplicationExceptionDetail customerException = new ApplicationExceptionDetail(ex.getMessage(),
				messageSource.getMessage(errMsgDetail, null, request.getLocale()),
				((ServletWebRequest) request).getRequest().getRequestURI().toString());
		return new ResponseEntity<ApplicationExceptionDetail>(customerException, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
