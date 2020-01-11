package com.java.app;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.java.app.domain.Customer;


/**
 * @author prasanna kumar
 *
 */
@Configuration
@ComponentScan("com.java.app")
@SpringBootApplication
@EntityScan(basePackageClasses=Customer.class)
public class AccessingDataJpaApplication {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(AccessingDataJpaApplication.class, args);
	}
}
