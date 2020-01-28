package com.java.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.java.app.config.SpringSecurityConfig;
import com.java.dao.Customer;
import com.java.dao.CustomerRepository;

/**
 * Integration test for /1/customer REST endpoints.
 * 
 * @author richmondchng
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { SpringSecurityConfig.class, TestIntegrationConfig.class })
@WebMvcTest
public class CustomerControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @After
    public void tearDown() {
        mockMvc = null;
        reset(customerRepository);
    }

    /**
     * Test /1/customer GET. Has customers.
     *
     * @throws Exception
     */
    @Test
    public void testGetCustomer_hasCustomers() throws Exception {
        when(customerRepository.findAll())
        .thenReturn(Arrays.asList(new Customer("Jack", "Barrel"), new Customer("Amy", "Choi")));

        // test
        mockMvc.perform(get("/1/customer").with(user("user").roles("USER"))).andDo(print()).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$").isArray())
        // record 1
        .andExpect(jsonPath("$[0].firstName").value("Jack")).andExpect(jsonPath("$[0].lastName").value("Barrel"))
        // record 2
        .andExpect(jsonPath("$[1].firstName").value("Amy")).andExpect(jsonPath("$[1].lastName").value("Choi"));

        // verify service is called
        verify(customerRepository, times(1)).findAll();
    }

    /**
     * Test /1/customer GET. No content.
     *
     * @throws Exception
     */
    @Test
    public void testGetCustomer_noContent() throws Exception {
        // test
        mockMvc.perform(get("/1/customer").with(user("user").roles("USER"))).andDo(print())
        .andExpect(status().isNoContent());

        // verify service is called
        verify(customerRepository, times(1)).findAll();
    }

    /**
     * Test /1/customer/{id} GET.
     *
     * @throws Exception
     */
    @Test
    public void testGetCustomer_specifiedCustomer() throws Exception {
        when(customerRepository.findById(anyLong())).thenReturn(new Customer("Amy", "Choi"));

        // test
        mockMvc.perform(get("/1/customer/2").with(user("user").roles("USER"))).andDo(print()).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.firstName").value("Amy")).andExpect(jsonPath("$.lastName").value("Choi"));

        // verify service is called
        verify(customerRepository, times(1)).findById(eq(2L));
    }

    /**
     * Test /1/customer/{id} GET. Invalid Id.
     *
     * @throws Exception
     */
    @Test
    public void testGetCustomer_specifiedCustomer_invalidId() throws Exception {
        when(customerRepository.findById(anyLong())).thenReturn(null);

        // test
        mockMvc.perform(get("/1/customer/2").with(user("user").roles("USER"))).andDo(print())
        .andExpect(status().isBadRequest());

        // verify service is called
        verify(customerRepository, times(1)).findById(eq(2L));
    }

    /**
     * Test /1/customer/{id} DELETE. Non numeric parameter
     *
     * @throws Exception
     */
    @Test
    public void testDeleteCustomer_nonNumericParam() throws Exception {

        // test
        mockMvc.perform(delete("/1/customer/a123").with(user("user").roles("USER"))).andDo(print())
        .andExpect(status().isBadRequest());

        // verify not called
        verify(customerRepository, times(0)).findById(eq(2L));
        // verify not called
        verify(customerRepository, times(0)).deleteById(anyLong());
    }

    /**
     * Test /1/customer/{id} DELETE. Invalid Id, e.g. customer does not exists.
     *
     * @throws Exception
     */
    @Test
    public void testDeleteCustomer_invalidId() throws Exception {
        when(customerRepository.findById(anyLong())).thenReturn(null);

        // test
        mockMvc.perform(delete("/1/customer/2").with(user("user").roles("USER"))).andDo(print())
        .andExpect(status().isBadRequest());

        verify(customerRepository, times(1)).findById(eq(2L));
        // verify not called
        verify(customerRepository, times(0)).deleteById(eq(2L));
    }

    /**
     * Test /1/customer/{id} DELETE. Valid Id.
     *
     * @throws Exception
     */
    @Test
    public void testDeleteCustomer_okay() throws Exception {
        when(customerRepository.findById(anyLong())).thenReturn(new Customer("Felicia", "King"));

        // test
        mockMvc.perform(delete("/1/customer/2").with(user("user").roles("USER"))).andDo(print())
        .andExpect(status().isOk());

        verify(customerRepository, times(1)).findById(eq(2L));
        // verify not called
        verify(customerRepository, times(1)).deleteById(eq(2L));
    }

    /**
     * Test /1/customer/{id} PUT.
     *
     * @throws Exception
     */
    @Test
    public void testCreateCustomer() throws Exception {

        final Customer created = new Customer("John", "Doe");
        when(customerRepository.save(any(Customer.class))).thenReturn(created);

        // test
        mockMvc.perform(put("/1/customer/").with(user("user").roles("USER")).contentType(MediaType.APPLICATION_JSON)
                .content("{ \"firstName\": \"John\", \"lastName\": \"Doe\" }")).andDo(print())
        .andExpect(status().isOk())
        // return created customer
        .andExpect(jsonPath("$.firstName").value("John")).andExpect(jsonPath("$.lastName").value("Doe"));

        // verify not called
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
}
