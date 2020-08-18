package com.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.app.AccessingDataJpaApplication;
import com.java.dao.Customer;
import com.java.dao.CustomerRepoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AccessingDataJpaApplication.class)
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @MockBean
    CustomerRepoService customerRepoService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetCustomer() throws Exception {
        Customer customer = new Customer("testGetFirstName", "testGetLastName");

        when(customerRepoService.getCustomerById(1)).thenReturn(customer);

        this.mockMvc.perform(get("/1/customer/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(customer.getFirstName())));
    }

    @Test
    public void testPutAddOrUpdateCustomer() throws Exception {
        Customer customer = new Customer("testGetFirstName", "testGetLastName");

        when(customerRepoService.addOrUpdateCustomer(ArgumentMatchers.any())).thenReturn(customer);

        String inputJson = mapToJson(customer);

        this.mockMvc.perform(put("/1/customer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(customer.getFirstName())));
    }

    @Test
    public void testDeleteCustomerNotFound() throws Exception {
        when(customerRepoService.getCustomerById(2)).thenReturn(null);

        this.mockMvc.perform(delete("/1/customer/2"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteCustomerSuccess() throws Exception {
        when(customerRepoService.getCustomerById(3)).thenReturn(new Customer("FoundFirstName", "FoundLastName"));

        this.mockMvc.perform(delete("/1/customer/3"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testPostAddNewCustomer() throws Exception {
        Customer customer = new Customer("NewFirstName", "NewLastName");

        String inputJson = mapToJson(customer);

        when(customerRepoService.addOrUpdateCustomer(ArgumentMatchers.any())).thenReturn(customer);

        this.mockMvc.perform(post("/1/customer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(customer.getFirstName())));
    }

    private String mapToJson(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}

