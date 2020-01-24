package com.java.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.java.dao.Customer;
import com.java.dao.CustomerRepoService;
import com.java.exception.UnknownCustomerException;

/**
 * Unit test CustomerController.
 *
 * @author richmondchng
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CustomerControllerTest {

    // private MockMvc mockMvc;

    @Mock
    private CustomerRepoService customerRepoService;
    @Captor
    private ArgumentCaptor<Customer> customerCaptor;

    private CustomerController customerController;

    @Before
    public void setUp() throws Exception {
        // create test instance
        customerController = new CustomerController(customerRepoService);
    }

    @After
    public void tearDown() throws Exception {
        reset(customerRepoService);
        customerRepoService = null;
    }

    /**
     * Test getCustomers. Has customers.
     */
    @Test
    public void testGetCustomers_hasCustomers() {
        // returns
        when(customerRepoService.getCustomers())
        .thenReturn(Arrays.asList(new Customer("Tim", "Alan"), new Customer("Joan", "Porters")));

        // invoke
        final ResponseEntity<Collection<Customer>> results = customerController.getCustomers();

        // verify
        verify(customerRepoService, times(1)).getCustomers();
        // test results
        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertNotNull(results.getBody());
        assertEquals(2, results.getBody().size());
        for (Customer c : results.getBody()) {
            if ("Tim".equals(c.getFirstName())) {
                assertEquals("Alan", c.getLastName());
                continue;
            }
            if ("Joan".equals(c.getFirstName())) {
                assertEquals("Porters", c.getLastName());
                continue;
            }
            fail("Unexpected customer " + c.getFirstName());
        }
    }

    /**
     * Test getCustomers. No customers.
     */
    @Test
    public void testGetCustomers_noCustomers() {
        // returns
        when(customerRepoService.getCustomers()).thenReturn(new ArrayList<>());

        // invoke
        final ResponseEntity<Collection<Customer>> results = customerController.getCustomers();

        // verify
        verify(customerRepoService, times(1)).getCustomers();
        // test results
        assertEquals(HttpStatus.NO_CONTENT, results.getStatusCode());
    }

    /**
     * Test getCustomer. By Id, valid customer.
     */
    @Test
    public void testGetCustomer() {
        // returns
        when(customerRepoService.getCustomerById(anyLong())).thenReturn(new Customer("Tim", "Alan"));

        // invoke
        final ResponseEntity<Customer> result = customerController.getCustomer("12");

        // verify
        verify(customerRepoService, times(1)).getCustomerById(12L);
        // test results
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        final Customer customer = result.getBody();
        assertEquals("Tim", customer.getFirstName());
        assertEquals("Alan", customer.getLastName());
    }

    /**
     * Test getCustomer. By Id, invalid Id.
     */
    @Test
    public void testGetCustomer_invalidId() {
        // returns
        when(customerRepoService.getCustomerById(anyLong())).thenReturn(null);

        // invoke
        final ResponseEntity<Customer> result = customerController.getCustomer("12");

        // verify
        verify(customerRepoService, times(1)).getCustomerById(12L);
        // test results
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    /**
     * Test getCustomer. By Id, non numeric Id.
     */
    @Test
    public void testGetCustomer_nonNumericId() {
        // returns
        when(customerRepoService.getCustomerById(anyLong())).thenReturn(null);

        // invoke
        final ResponseEntity<Customer> result = customerController.getCustomer("e1234");

        // verify
        verify(customerRepoService, times(0)).getCustomerById(anyLong());
        // test results
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    /**
     * Test addOrUpdateCustomer.
     */
    @Test
    public void testAddOrUpdateCustomer() {
        doAnswer(new Answer<Customer>() {
            @Override
            public Customer answer(final InvocationOnMock invocation) throws Throwable {
                // return itself
                Customer param = (Customer) invocation.getArguments()[0];
                return param;
            }

        }).when(customerRepoService).addOrUpdateCustomer(any(Customer.class));

        // invoke
        final ResponseEntity<Customer> result = customerController.addOrUpdateCustomer(new Customer("Joan", "Alan"));
        // verify
        verify(customerRepoService, times(1)).addOrUpdateCustomer(customerCaptor.capture());
        // test results
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        final Customer customer = result.getBody();
        assertEquals("Joan", customer.getFirstName());
        assertEquals("Alan", customer.getLastName());
        // test argument
        final Customer argument = customerCaptor.getValue();
        assertNotNull(argument);
        assertEquals("Joan", argument.getFirstName());
        assertEquals("Alan", argument.getLastName());
    }

    /**
     * Test deleteCustomer.
     *
     * @throws Exception
     */
    @Test
    public void testDeleteCustomer() throws Exception {
        // invoke
        final ResponseEntity<String> result = customerController.deleteCustomer("123");
        // verify
        verify(customerRepoService, times(1)).deleteExistingCustomer(123L);
        // test results
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Success", result.getBody());
    }

    /**
     * Test deleteCustomer. Invalid customer Id.
     *
     * @throws Exception
     */
    @Test
    public void testDeleteCustomer_unknownCustomer() throws Exception {
        // throws an exception if called
        doThrow(new UnknownCustomerException()).when(customerRepoService).deleteExistingCustomer(anyLong());

        // invoke
        final ResponseEntity<String> result = customerController.deleteCustomer("123");
        // verify
        verify(customerRepoService, times(1)).deleteExistingCustomer(123L);
        // test results
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    /**
     * Test deleteCustomer. Non numeric customer Id.
     *
     * @throws Exception
     */
    @Test
    public void testDeleteCustomer_nonNumericId() throws Exception {
        // invoke
        final ResponseEntity<String> result = customerController.deleteCustomer("e123");
        // verify
        verify(customerRepoService, times(0)).deleteExistingCustomer(anyLong());
        // test results
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
}
