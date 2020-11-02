package com.java.app;
import com.java.controller.CustomerController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableJpaRepositories("com...dao")
@SpringBootApplication(
	scanBasePackages={"com...app", "com...dao"},
	scanBasePackageClasses = {CustomerController.class}
)
public class AccessingDataJpaApplication {
	public static void main(String[] args) {
		SpringApplication.run(AccessingDataJpaApplication.class, args);
	}
}
