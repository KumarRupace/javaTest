package com.customer.journey.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.math.NumberUtils;

public class CustomerIdValidator implements ConstraintValidator<CheckCustomerId, String> {

	@Override
	public boolean isValid(String customerId, ConstraintValidatorContext context) {
		
		 return (customerId != null && NumberUtils.isParsable(customerId));
	}

}
