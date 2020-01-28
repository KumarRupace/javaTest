package com.java.controller;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.java.dao.CustomerRepoService;
import com.java.dao.CustomerRepository;

/**
 * Integration test configuration.
 * 
 * @author richmondchng
 *
 */
@Configuration
@ComponentScan(basePackages = { "com.java.controller" })
public class TestIntegrationConfig {

    @Bean
    public CustomerRepoService customerRepoService() {
        return new CustomerRepoService();
    }

    @Bean
    public CustomerRepository customerRepository() {
        return Mockito.mock(CustomerRepository.class);
    }

}
