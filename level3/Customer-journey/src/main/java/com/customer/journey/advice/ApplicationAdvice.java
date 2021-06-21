package com.customer.journey.advice;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.customer.journey.exception.ApplicationException;
import com.customer.journey.util.CustomerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jhonson
 *
 */
@Aspect
@Component
public class ApplicationAdvice {

	private static final Logger log = LoggerFactory.getLogger(ApplicationAdvice.class);

	@Value(CustomerConstants.GET_ALLOWED_URI_LIST)
	private List<String> accessibleUri;

	@Autowired
	private HttpServletRequest request;

	/**
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 * 
	 * This around advice will run around the controller pointcuts to validate the request path. 
	 * Advice can be used for many such cases to intercept the pointcuts and have our own operation executed.   
	 */
	@Around(value = "execution(* com.customer.journey.controller.CustomerController.*(..))")

	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

		log.info("ApplicationAdvice :: Invoking Method :: '" + joinPoint.getSignature().getName() + "' Starts");
		long start = System.currentTimeMillis();

		Object proceed = null;

		if (accessibleUri.toString().contains(request.getRequestURI().toString())
				|| request.getRequestURI().toString().startsWith(CustomerConstants.PATH_HAS_FIND_BY_ID))
			
			proceed = joinPoint.proceed();
	
		else {
			
			log.info("ApplicationAdvice :: Invalid Request Path :: Throwing Exception for tracking");
			throw new ApplicationException(CustomerConstants.RESOURCE_ACCESS_VIOLATION);
		
		}

		long executionTime = System.currentTimeMillis() - start;

		log.info("ApplicationAdvice :: Invoking Method :: '" + joinPoint.getSignature().getName() + "' Ends");
		log.info("ApplicationAdvice :: The Method :: '" + joinPoint.getSignature().getName() + "' has been Executed in "
				+ executionTime + "ms");

		return proceed;
	}

	/**
	 * @param joinPoint
	 * @param ex
	 * @throws Throwable
	 */
	@AfterThrowing(value = "execution(* com.customer.journey.controller.CustomerController.*(..))", throwing = "ex")
	public void logTheApplicationErr(JoinPoint joinPoint, Exception ex) throws Throwable {

		log.error("ApplicationAdvice :: The Method :: '" + joinPoint.getSignature().getName()
				+ " has ended up with exception :: " + ex.getMessage());

	}

	/**
	 * @param joinPoint
	 * @param retVal
	 */
	@AfterReturning(value = "execution(* com.customer.journey.controller.CustomerController.*(..))", returning = "retVal")
	public void logTheApplicationReturns(JoinPoint joinPoint, Object retVal) {

		log.info("ApplicationAdvice :: The Method :: '" + joinPoint.getSignature().getName() + "' " + "has returned :: "
				+ retVal);

	}

}
