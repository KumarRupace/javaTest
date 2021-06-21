package com.customer.journey.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.customer.journey.domain.Customer;
import com.customer.journey.util.CustomerConstants;

@Component
public class CustomerValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
	
		return Customer.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		if (target instanceof Customer) {
			
			Customer user = (Customer) target;
	        if (CustomerConstants.checkInputString(user.getFirstName())) 
	              errors.reject(CustomerConstants.INVALID_FIRSTNAME);
	       
	        if (CustomerConstants.checkInputString(user.getLastName())) 
	        	errors.reject(CustomerConstants.INVALID_LASTNAME);
		 } 
	}
}
