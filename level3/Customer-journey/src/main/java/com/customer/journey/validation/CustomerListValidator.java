package com.customer.journey.validation;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.customer.journey.domain.Customer;
import com.customer.journey.util.CustomerConstants;

public class CustomerListValidator implements ConstraintValidator<CheckCustomerList, List<Customer>> {

	@Override
	public boolean isValid(List<Customer> value, ConstraintValidatorContext context) {

		return ((List<Customer>) value).stream().filter(
				customer -> CustomerConstants.checkInputString(customer.getFirstName()) 
								|| CustomerConstants.checkInputString(customer.getLastName()))
				.collect(Collectors.toList()).size() == 0;

	}
}
