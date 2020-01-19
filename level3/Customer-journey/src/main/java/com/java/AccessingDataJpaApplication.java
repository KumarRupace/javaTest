package com.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Application starter
 * 
 * Moving application starter to com.java package, which will ensure all the
 * package will be auto scan under com.java
 * 
 * @author Kimi Qian Min
 *
 */
@Configuration
@SpringBootApplication
public class AccessingDataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccessingDataJpaApplication.class, args);
	}

	/**
	 * Initial passwordEncoder bean for password comparison
	 * 
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
