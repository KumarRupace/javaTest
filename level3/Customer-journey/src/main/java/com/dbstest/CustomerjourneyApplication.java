package com.dbstest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
/**
 * @author Henry
 */

@SpringBootApplication
@EntityScan(basePackages = "com.dbstest")
@ComponentScan(basePackages = "com.dbstest")
@EnableJpaRepositories("com.dbstest")
public class CustomerjourneyApplication {
	public static void main(String[] args) {
		SpringApplication.run(CustomerjourneyApplication.class, args);
	}

}
