package com.assesmentdbs.customerjourney.service;

import com.assesmentdbs.customerjourney.exceptions.CustomerAlreadyPresentException;
import com.assesmentdbs.customerjourney.exceptions.CustomerNotFoundException;
import com.assesmentdbs.customerjourney.model.Customer;
import com.assesmentdbs.customerjourney.repository.CustomerRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

/**
 * @author Prithvi Panchapakeshan
 * All unit test cases for CustomerRepoService.
 */
class CustomerRepoServiceTest {

    @Mock
    CustomerRepo customerRepo;
    @InjectMocks
    CustomerRepoService customerRepoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllCustomers() {
        Mockito.when(customerRepo.findAll()).thenReturn(Arrays.asList(new Customer()));
        List<Customer> allCustomers = customerRepoService.getAllCustomers();
        assertEquals(1,allCustomers.size());
    }

    @Test
    void returnCustomerWithNameTimWhenQueriedWithId1() throws CustomerNotFoundException {
        Mockito.when(customerRepo.findById(1l)).thenReturn(
                Optional.of(new Customer("Tim","Cook"))
        );
        Customer customer = customerRepoService.getCustomerById(1l);
        Mockito.verify(customerRepo,Mockito.times(1)).findById(1l);
        assertEquals("Tim",customer.getFirstName());
    }

    @Test
    void failWhenCustomerIsNotFound() {
        Mockito.when(customerRepo.findById(1l)).thenThrow(
                new CustomerNotFoundException("")
        );
        assertThrows(CustomerNotFoundException.class, () -> customerRepo.findById(1l));
    }

    @Test
    void addCustomer(){
        Customer customer = new Customer("Tim", "Cook");
        Mockito.when(customerRepo.save(customer)).thenReturn(customer);
        Customer customerSaved = customerRepoService.addCustomer(customer);
        Mockito.verify(customerRepo,Mockito.times(1)).save(customer);
        assertEquals(customer.getLastName(),customerSaved.getLastName());
    }

    @Test
    void updateCustomerSuccessfully(){
        Customer customer = new Customer("John", "Foo");
        customer.setId(1l);
        Mockito.when(customerRepo.findById(1l))
                .thenReturn(Optional.of(new Customer()));
        Mockito.when(customerRepo.save(Mockito.any()))
                .thenReturn(customer);
        Customer customerById = customerRepoService.updateCustomer(1l,customer);
        assertEquals(customerById.getFirstName(),"John");
        Mockito.verify(customerRepo,times(1)).findById(Mockito.any());
        Mockito.verify(customerRepo,times(1)).save(Mockito.any());
    }

    @Test
    void failWhenTryingToAddCustomerWhoAlreadyExists(){
        Customer customer = new Customer("John", "Foo");
        customer.setId(1l);
        Mockito.when(customerRepo.findById(Mockito.any()))
                .thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerRepoService.updateCustomer(1l,customer));
        Mockito.verify(customerRepo,times(1)).findById(Mockito.any());
        Mockito.verify(customerRepo,never()).save(Mockito.any());
    }

    @Test
    void deleteCustomer() {
        Customer customer = new Customer("John", "Foo");
        customer.setId(2l);
        Mockito.when(customerRepo.findById(2l))
                .thenReturn(Optional.of(customer));
        Customer customerById = customerRepoService.delete(2l);
        assertEquals(customerById.getFirstName(),"John");
        Mockito.verify(customerRepo,times(1)).findById(Mockito.any());
        Mockito.verify(customerRepo,times(1)).delete(Mockito.any());
    }

}