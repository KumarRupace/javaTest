package com.assesmentdbs.customerjourney;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Prithvi Panchapakeshan
 */
@SpringBootApplication
@EnableSwagger2
public class CustomerJourneyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerJourneyApplication.class, args);
	}

}
