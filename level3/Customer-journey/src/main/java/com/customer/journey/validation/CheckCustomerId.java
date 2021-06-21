package com.customer.journey.validation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import com.customer.journey.util.CustomerConstants;

@Target({ElementType.PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = CustomerIdValidator.class)
@Documented

public @interface CheckCustomerId {

	String message() default CustomerConstants.CHK_CUST_ID_DEFAULT_MSG;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
}
