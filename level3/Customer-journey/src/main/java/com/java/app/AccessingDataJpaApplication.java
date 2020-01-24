package com.java.app;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;

@SpringBootApplication
@EntityScan(basePackages = { "com.java.dao" })
@EnableJpaRepositories(basePackages = {"com.java.dao" }, 
    includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
        classes = {CrudRepository.class }))
@ComponentScan(basePackages = { "com.java.dao", "com.java.controller", "com.java.app" })
public class AccessingDataJpaApplication {
    public static void main(final String[] args) {
        SpringApplication.run(AccessingDataJpaApplication.class, args);
    }
}
