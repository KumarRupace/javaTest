package com.java.app;

import com.java.dao.Customer;
import com.java.dao.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadUserData();
    }

    private void loadUserData() {
        if (customerRepository.count() == 0) {
            Customer customer1 = new Customer("John", "Doe");
            Customer customer2 = new Customer("Johny", "Walker");
            Customer customer3 = new Customer("Gulwal", "Manohar");
            customerRepository.save(customer1);
            customerRepository.save(customer2);
            customerRepository.save(customer3);
        }
        System.out.println(String.format("Seed Customer Count : %d", customerRepository.count()));
    }
}
