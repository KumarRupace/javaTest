package com.java.dao;

import com.java.entity.Customer;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CustomerRepoServiceTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    CustomerRepository customerRepository;


    @Test
    public void testNoCustomersFound() {
        Iterable<Customer> customers = customerRepository.findAll();

        assertThat(customers).isEmpty();
    }

    @Test
    public void testRepository()
    {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Smith");

        customerRepository.save(customer);

        Assert.assertNotNull(customer.getId());
    }

    @Test
    public void testAllCustomers() {
        Customer customer1 = new Customer("John", "Smith");
        testEntityManager.persist(customer1);

        Customer customer2 = new Customer("Mary", "Kumar");
        testEntityManager.persist(customer2);

        Customer customer3 = new Customer("SS", "Wong");
        testEntityManager.persist(customer3);

        Iterable<Customer> customers = customerRepository.findAll();

        assertThat(customers).hasSize(3).contains(customer1, customer2, customer3);
    }

    @Test
    public void testCustomerById() {
        Customer customer1 = new Customer("John", "Smith");
        testEntityManager.persist(customer1);

        Customer customer2 = new Customer("Mary", "Kumar");
        testEntityManager.persist(customer2);

        Optional<Customer> foundCustomer = customerRepository.findById(customer2.getId());

        assertThat(foundCustomer.get()).isEqualTo(customer2);
    }

    @Test
    public void testUpdateCustomer() {
        Customer customer1 = new Customer("John", "Smith");
        testEntityManager.persist(customer1);

        Optional<Customer> foundCustomer = customerRepository.findById(customer1.getId());

        foundCustomer.get().setFirstName("Adam");

        assertThat(foundCustomer.get().getFirstName()).isEqualTo("Adam");
    }

    @Test
    public void testDeleteCustomer() {
        Customer customer1 = new Customer("John", "Smith");
        testEntityManager.persist(customer1);

        Customer customer2 = new Customer("Mary", "Kumar");
        testEntityManager.persist(customer2);

        Customer customer3 = new Customer("SS", "Wong");
        testEntityManager.persist(customer3);

        customerRepository.deleteById(1L);

        assertEquals(2L, customerRepository.count());
    }


}
