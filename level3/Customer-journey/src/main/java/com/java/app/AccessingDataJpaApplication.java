package com.java.app;
import com.java.controller.CustomerController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.java")
@EnableJpaRepositories("com.java.dao")
@EntityScan("com.java.entity")
public class AccessingDataJpaApplication {
	public static void main(String[] args) {
		SpringApplication.run(AccessingDataJpaApplication.class, args);
	}
}
