package com.assesmentdbs.customerjourney.service;

import com.assesmentdbs.customerjourney.exceptions.CustomerNotFoundException;
import com.assesmentdbs.customerjourney.model.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Prithvi Panchapakeshan
 * Integration test cases for CustomerRepoService.
 */
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerRepoServiceIntegrationTest {
    @Autowired
    CustomerRepoService customerRepoService;

    @Test
    public void contextLoads() throws Exception{
        assertNotNull(customerRepoService);
    }

    @Test
    public void whenQueriedForAnExistingCustomerShouldReturnCustomer(){
        Customer customerById = customerRepoService.getCustomerById(3l);
        assertEquals("Jane",customerById.getFirstName());
    }

    @Test
    public void whenQueriedForNonExistingCustomerShouldReturnException(){
        assertThrows(CustomerNotFoundException.class,
                () -> customerRepoService.getCustomerById(101l));
    }

    @Test
    public void whenQueriedForAllCustomersShouldReturnAllCustomersInDb(){
        List<Customer> allCustomers = customerRepoService.getAllCustomers();
        assertEquals(6,allCustomers.size());
    }

    @Test
    public void whenNameDateIsUpdatedDbShouldHaveUpdatedRecord(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
        Customer customerById = customerRepoService.getCustomerById(1l);
        customerById.setFirstName("Rick");
        customerById.setLastName("Ong");
        try {
            customerById.setDateOfBirth(simpleDateFormat.parse("03-09-1985"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        customerRepoService.updateCustomer(1l,customerById);
        Customer updatedCustomer = customerRepoService.getCustomerById(1l);
        assertEquals("Rick",updatedCustomer.getFirstName());
        assertEquals("Ong",updatedCustomer.getLastName());
    }

    @Test
    public void whenDeletedCustomerShouldNotBeFound(){
        customerRepoService.delete(3l);
        assertThrows(CustomerNotFoundException.class,
                () -> customerRepoService.getCustomerById(3l));
    }
}
