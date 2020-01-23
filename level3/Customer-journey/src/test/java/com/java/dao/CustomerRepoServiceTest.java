package com.java.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Unit test CustomerRepoService
 *
 * @author richmondchng
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CustomerRepoServiceTest {

    @Captor
    private ArgumentCaptor<Customer> customerArgCaptor;

    @Mock
    private CustomerRepository mCustomerRepository;

    @InjectMocks
    private CustomerRepoService customerRepoService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        reset(mCustomerRepository);
    }

    /**
     * Test addOrUpdateCustomer.
     */
    @Test
    public void testAddOrUpdateCustomer() {
        // invoke
        customerRepoService.addOrUpdateCustomer(new Customer("Jack", "Russell"));

        // verify repository is called
        verify(mCustomerRepository, times(1)).save(customerArgCaptor.capture());
        // test argument
        final Customer result = customerArgCaptor.getValue();
        assertNotNull(result);
        assertEquals("Jack", result.getFirstName());
        assertEquals("Russell", result.getLastName());
    }

    /**
     * Test deleteExistingCustomer.
     */
    @Test
    public void testDeleteExistingCustomer() {
        // return valid bean
        when(mCustomerRepository.findById(anyLong())).thenReturn(new Customer("Jack", "Russell"));

        // invoke
        customerRepoService.deleteExistingCustomer(1099L);
        // verify repository
        verify(mCustomerRepository, times(1)).findById(eq(1099L));
        verify(mCustomerRepository, times(1)).deleteById(eq(1099L));
    }

    /**
     * Test deleteExistingCustomer. Invalid customer Id.
     */
    @Test
    public void testDeleteExistingCustomer_customerNotFound() {
        // return valid bean
        when(mCustomerRepository.findById(anyLong())).thenReturn(null);

        try {
            // invoke
            customerRepoService.deleteExistingCustomer(1099L);
            fail("Expect exception");
        } catch (Exception e) {
            // verify repository
            verify(mCustomerRepository, times(1)).findById(eq(1099L));
            verify(mCustomerRepository, times(0)).deleteById(anyLong());
        }
    }

    /**
     * Test getCustomerById.
     */
    @Test
    public void testGetCustomerById() {
        // return valid bean
        when(mCustomerRepository.findById(anyLong())).thenReturn(new Customer("Jack", "Russell"));

        // invoke
        final Customer result = customerRepoService.getCustomerById(1099L);

        // verify repository
        verify(mCustomerRepository, times(1)).findById(eq(1099L));
        // test
        assertNotNull(result);
        assertEquals("Jack", result.getFirstName());
        assertEquals("Russell", result.getLastName());
    }

    /**
     * Test deleteExistingCustomer. Invalid customer Id.
     */
    @Test
    public void testGetCustomerById_customerNotFound() {
        // return valid bean
        when(mCustomerRepository.findById(anyLong())).thenReturn(null);

        // invoke
        final Customer result = customerRepoService.getCustomerById(1099L);
        // verify repository
        verify(mCustomerRepository, times(1)).findById(eq(1099L));
        // test
        assertNull(result);
    }

    /**
     * Test getCustomers.
     */
    @Test
    public void testGetCustomers() {
        // return list
        when(mCustomerRepository.findAll())
        .thenReturn(Arrays.asList(new Customer("Jack", "Russell"), new Customer("Jenny", "Wilson")));

        // invoke
        final Collection<Customer> results = customerRepoService.getCustomers();
        // verify
        verify(mCustomerRepository, times(1)).findAll();
        // test
        assertNotNull(results);
        assertEquals(2, results.size());

        final Iterator<Customer> itr = results.iterator();
        final Customer customer1 = itr.next();
        assertNotNull(customer1);
        assertEquals("Jack", customer1.getFirstName());
        assertEquals("Russell", customer1.getLastName());

        final Customer customer2 = itr.next();
        assertNotNull(customer2);
        assertEquals("Jenny", customer2.getFirstName());
        assertEquals("Wilson", customer2.getLastName());
    }

    /**
     * Test getCustomers. No customers.
     */
    @Test
    public void testGetCustomers_noCustomer() {
        // return list
        when(mCustomerRepository.findAll()).thenReturn(new ArrayList<>());

        // invoke
        final Collection<Customer> results = customerRepoService.getCustomers();
        // verify
        verify(mCustomerRepository, times(1)).findAll();
        // test
        assertNotNull(results);
        assertEquals(0, results.size());
    }
}
