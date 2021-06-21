package com.customer.journey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Jhonson
 *
 * This is the main class where the application starts!
 */
@SpringBootApplication()
@EnableWebMvc
public class CustomerJourneyApplication {

	@Autowired
    private DispatcherServlet servlet;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(CustomerJourneyApplication.class, args);
		
	}
	
	 /**
	 * @param context
	 * @return
	 * 
	 * This method has been created to capture container exception while application running
	 */
	@Bean
    public CommandLineRunner getCommandLineRunner(ApplicationContext context) {
	        servlet.setThrowExceptionIfNoHandlerFound(true);
	        return args -> {};
	 }
	
}
