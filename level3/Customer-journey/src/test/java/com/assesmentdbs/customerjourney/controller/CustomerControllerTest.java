package com.assesmentdbs.customerjourney.controller;

import com.assesmentdbs.customerjourney.exceptions.CustomerAlreadyPresentException;
import com.assesmentdbs.customerjourney.exceptions.CustomerNotFoundException;
import com.assesmentdbs.customerjourney.model.Customer;
import com.assesmentdbs.customerjourney.service.CustomerRepoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

/**
 * @author Prithvi Panchapakeshan
 * All unit test cases for CustomerController.
 */
class CustomerControllerTest {

    @Mock
    CustomerRepoService customerRepoService;

    CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        customerController = new CustomerController(customerRepoService);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllCustomers() {
        Mockito.when(customerRepoService.getAllCustomers())
                .thenReturn(Arrays.asList(new Customer()));
        List<Customer> allCustomers = customerController.getAllCustomers();
        assertEquals(1,allCustomers.size());
        Mockito.verify(customerRepoService,times(1)).getAllCustomers();

    }

    @Test
    void returnCustomerWithNameJohnWhenQueriedWithId1(){
        Mockito.when(customerRepoService.getCustomerById(1l))
                .thenReturn(new Customer("John","Foo"));
        Customer customerById = customerController.getCustomerById(1l);
        assertEquals(customerById.getFirstName(),"John");
        Mockito.verify(customerRepoService,times(1)).getCustomerById(1l);
    }

    @Test
    void failWhenCustomerIsNotFound(){
        Mockito.when(customerRepoService.getCustomerById(1l))
                .thenThrow(new CustomerNotFoundException("Not found"));
        assertThrows(CustomerNotFoundException.class,
                () -> customerController.getCustomerById(1l));
    }

    @Test
    void addCustomer(){
        Customer customer = new Customer("John", "Foo");
        Mockito.when(customerRepoService.addCustomer(customer))
                .thenReturn(customer);
        Customer customerById = customerController.addCustomer(customer);
        assertEquals(customerById.getFirstName(),"John");
        Mockito.verify(customerRepoService,times(1))
                .addCustomer(Mockito.any());
    }

    @Test
    void updateCustomerSuccessfully(){
        Customer customer = new Customer("John", "Foo");
        Mockito.when(customerRepoService.updateCustomer(1l,customer))
                .thenReturn(customer);
        Customer customerById = customerController.updateCustomer(1l,customer);
        assertEquals(customerById.getFirstName(),"John");
        Mockito.verify(customerRepoService,times(1)).updateCustomer(Mockito.any(),Mockito.any());
    }

    @Test
    void failWhenTryingToAddCustomerWhoAlreadyExists(){
        Customer customer = new Customer("John", "Foo");
        Mockito.when(customerRepoService.addCustomer(customer))
                .thenThrow(new CustomerAlreadyPresentException("Customer already present"));
        assertThrows(CustomerAlreadyPresentException.class,
                () -> customerController.addCustomer(customer));
        Mockito.verify(customerRepoService,times(1))
                .addCustomer(customer);
    }

    @Test
    void deleteCustomer() {
        Customer customer = new Customer("John", "Foo");
        Mockito.when(customerRepoService.delete(3l))
                .thenReturn(customer);
        Customer customerById = customerController.deleteCustomer(3l);
        assertEquals(customerById.getFirstName(),"John");
        Mockito.verify(customerRepoService,times(1))
                .delete(Mockito.any());
    }

}