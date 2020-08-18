package com.java;

import com.java.app.AccessingDataJpaApplication;
import com.java.dao.Customer;
import com.java.dao.CustomerRepoService;
import com.java.dao.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AccessingDataJpaApplication.class)
@AutoConfigureMockMvc
public class CustomerRepoServiceTest {
    @MockBean
    CustomerRepository customerRepository;

    @Autowired
    private CustomerRepoService customerRepoService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getCustomerByIdSuccesfully() {
        Customer customerInDb = new Customer("foundFirstName", "foundLastName");

        given(customerRepository.findById(1)).willReturn(customerInDb);

        Customer customerToBeFound = customerRepoService.getCustomerById(1);

        assertNotNull(customerToBeFound);

        assertEquals(customerToBeFound, customerInDb);
    }

    @Test
    public void getCustomerByIdNotFound() {
        given(customerRepository.findById(1)).willReturn(null);

        Customer customerToBeFound = customerRepoService.getCustomerById(1);

        assertNull(customerToBeFound);
    }

    @Test
    public void addOrUpdateCustomerSuccessfully() {
        Customer customer = new Customer("FirstName", "LastName");

        given(customerRepository.save(customer)).willAnswer(invocation -> invocation.getArgument(0));

        Customer addOrUpdatedUser = customerRepoService.addOrUpdateCustomer(customer);

        assertNotNull(addOrUpdatedUser);

        assertEquals(addOrUpdatedUser, customer);
    }

    @Test
    public void deleteCustomerSuccessfully() {
        customerRepoService.deleteCustomer(1);

        verify(customerRepository).deleteById(1L);
    }
}
