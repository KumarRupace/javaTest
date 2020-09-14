package com.java.app.dao;

import com.java.exception.IllegalCustomerArgumentException;
import com.java.dao.Customer;
import com.java.dao.CustomerRepoService;
import com.java.dao.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CustomerRepoServiceTest {
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerRepoService customerRepoService;

    @Test
    public void testGetCustomerById() {
        Customer customer = new Customer(FIRST_NAME, LAST_NAME);
        Mockito.when(customerRepository.findById(Mockito.anyLong())).thenReturn(customer);
        Customer result = customerRepoService.getCustomerById(1);
        Mockito.verify(customerRepository).findById(Mockito.anyLong());
        assertEquals(customer, result);
    }

    @Test
    public void testCreateCustomerByFirstLastName()  {
        Customer customer = new Customer(FIRST_NAME, LAST_NAME);
        Mockito.when(customerRepository.save(Mockito.any())).thenReturn(customer);
        Customer result = customerRepoService.createCustomerByFirstLastName(FIRST_NAME, LAST_NAME);
        Mockito.verify(customerRepository).save(Mockito.any());
        assertEquals(customer, result);
    }

    @Test(expected = IllegalCustomerArgumentException.class)
    public void testUpdateOrCreateCustomerWithException() {
        customerRepoService.updateOrCreateCustomer(null);
    }

    @Test
    public void testUpdateOrCreateCustomer() {
        Customer customer = new Customer(FIRST_NAME, LAST_NAME);
        Mockito.when(customerRepository.save(Mockito.any())).thenReturn(customer);
        Customer result = customerRepoService.updateOrCreateCustomer(customer);
        Mockito.verify(customerRepository).save(Mockito.any());
        assertEquals(customer, result);
    }

    @Test
    public void testDeleteCustomerById() {
        Mockito.doNothing().when(customerRepository).deleteById(Mockito.anyLong());
        customerRepoService.deleteCustomerById(1);
        Mockito.verify(customerRepository).deleteById(Mockito.anyLong());
    }

    @Test(expected = IllegalCustomerArgumentException.class)
    public void testFindCustomersByLastNameWithNull() {
        customerRepoService.findCustomersByLastName(null);
    }

    @Test
    public void testFindCustomersByLastName() {
        Customer customer = new Customer(FIRST_NAME, LAST_NAME);
        List<Customer> list = Collections.singletonList(customer);
        Mockito.when(customerRepository.findByLastName(Mockito.anyString())).thenReturn(list);
        List<Customer> result = customerRepoService.findCustomersByLastName("test");
        Mockito.verify(customerRepository).findByLastName(Mockito.anyString());
        assertEquals(list, result);

    }
}
