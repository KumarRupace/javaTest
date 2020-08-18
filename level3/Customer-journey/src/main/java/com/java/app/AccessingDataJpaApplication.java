package com.java.app;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.java.controller", "com.java.dao"})
@EntityScan(basePackages = {"com.java.dao"})
@EnableJpaRepositories("com.java.dao")
public class AccessingDataJpaApplication {
	public static void main(String[] args) {
		SpringApplication.run(AccessingDataJpaApplication.class, args);
	}
}
