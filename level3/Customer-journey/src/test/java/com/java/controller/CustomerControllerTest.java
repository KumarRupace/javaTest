package com.java.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.entity.Customer;
import com.java.service.CustomerRepoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {CustomerController.class})
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepoService customerRepoService;

    @Autowired
    CustomerController customerController;


    private static final long UNKNOWN_ID = Long.MAX_VALUE;

    @Test
    public void contextLoads() throws Exception{
        assertNotNull(customerController);
    }

    @Test
    public void getAllCustomers() throws Exception
    {
        List<Customer> customers = Arrays.asList(
                new Customer(1L, "John", "Smith"),
                new Customer(2L, "Mary", "Kumar" ));

        when(customerRepoService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/customer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Smith")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Mary")))
                .andExpect(jsonPath("$[1].lastName", is("Kumar")));

        verify(customerRepoService, times(1)).getAllCustomers();
        verifyNoMoreInteractions(customerRepoService);

    }

    @Test
    public void testGetCustomer_OK() throws Exception {
        Customer customer = new Customer(Long.valueOf(1),"John","Smith");
        when(customerRepoService.getCustomer(customer.getId())).thenReturn(customer);
        mockMvc.perform(get("/customer/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Smith")));
        verify(customerRepoService, times(1)).getCustomer(customer.getId());
        verifyNoMoreInteractions(customerRepoService);
    }

    @Test
    public void testGetCustomer_NotFound() throws Exception {
        Customer customer = new Customer(Long.valueOf(1),"John","Smith");
        when(customerRepoService.getCustomer(customer.getId())).thenReturn(customer);
        mockMvc.perform(get("/customer/{id}", 2))
                .andExpect(status().isNotFound());
        verify(customerRepoService, times(1)).getCustomer(2L);
        verifyNoMoreInteractions(customerRepoService);
    }


    @Test
    public void testUpdateCustomer_OK() throws Exception {
        Customer customer = new Customer(1L,"John","Smith");

        when(customerRepoService.getCustomer(customer.getId())).thenReturn(customer);
        doNothing().when(customerRepoService).updateCustomer(customer.getId(),customer);
        mockMvc.perform(put("/customer/{id}", customer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customer)))
                .andExpect(status().isOk());

       verify(customerRepoService, times(1)).updateCustomer(anyLong(), anyObject());
       verifyNoMoreInteractions(customerRepoService);
    }

    @Test
    public void testUpdateCustomer_NotFound() throws Exception {
        Customer customer = new Customer(UNKNOWN_ID, null,null);

        when(customerRepoService.getCustomer(customer.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/customer/{id}", customer.getId()))
                .andExpect(status().isNotFound());

        verify(customerRepoService, times(1)).getCustomer(customer.getId());
        verifyNoMoreInteractions(customerRepoService);
    }


    @Test
    public void testDeleteCustomer_OK() throws Exception {
        Customer customer = new Customer(1L,"John","Smith");

        when(customerRepoService.getCustomer(customer.getId())).thenReturn(customer);
        doNothing().when(customerRepoService).deleteById(customer.getId());

        mockMvc.perform(
                delete("/customer/{id}", customer.getId()))
                .andExpect(status().isOk());

        verify(customerRepoService, times(1)).getCustomer(customer.getId());
        verify(customerRepoService, times(1)).deleteById(customer.getId());
        verifyNoMoreInteractions(customerRepoService);
    }

    @Test
    public void testDeleteCustomer_NotFound() throws Exception {
        Customer customer = new Customer(UNKNOWN_ID, null,null);

        when(customerRepoService.getCustomer(customer.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/customer/{id}", customer.getId()))
                .andExpect(status().isNotFound());

        verify(customerRepoService, times(1)).getCustomer(customer.getId());
        verifyNoMoreInteractions(customerRepoService);
    }



    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
