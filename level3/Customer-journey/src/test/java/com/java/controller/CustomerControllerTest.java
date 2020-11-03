package com.java.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.app.AccessingDataJpaApplication;
import com.java.dao.Customer;
import com.java.dao.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import static com.java.app.Constants.MSG_INVALID_USER_ID;
import static com.java.app.Constants.MSG_NO_RECORD_FOUND;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = AccessingDataJpaApplication.class)
public class CustomerControllerTest {

    private static final String CUSTOMER_ID_2_FIRST_NAME = "Wei Cheng";
    private static final String CUSTOMER_ID_2_LAST_NAME = "Kow";

    private static final String NEW_CUSTOMER_FIRST_NAME = "Jun Wei";
    private static final String NEW_CUSTOMER_LAST_NAME = "Chang";

    private static final String VERY_LONG_NAME = "Anderson Jason Andrew James Samuel";
    private static final String INVALID_NAME = "@Jun W3i";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CustomerRepository mCustomerRepository;

    /**
     * Test API - Get Customer
     * Input: Id = Empty String
     * Expected: NestedServletException with ConstraintException's message "pId: must not be blank"
     * */
    @Test
    public void testGetCustomerRecord_WhenUserIdIsEmptyString_ShouldThrowNestedServletException() {
        Throwable thrown = catchThrowable(() -> {
            mockMvc.perform(get("/1/customer/ ").contentType(APPLICATION_JSON));
        });

        assertThat(thrown)
                .isInstanceOf(NestedServletException.class)
                .hasMessageContaining("pId: must not be blank");
    }

    /**
     * Test API - Get Customer
     * Input: Id = non-numeric
     * Expected: 400 Bad Request with message "Invalid User Id"
     * */
    @Test
    public void testGetCustomerRecord_WithInvalidUserId() throws Exception {
        mockMvc.perform(get("/1/customer/100a")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MSG_INVALID_USER_ID));
    }

    /**
     * Test API - Get Customer
     * Input: Id = numeric value but not exist in database
     * Expected: 200 OK with message "No record found"
     * */
    @Test
    public void testGetCustomerRecord_WhenUserIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/1/customer/100")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(MSG_NO_RECORD_FOUND));
    }

    /**
     * Test API - Get Customer
     * Input: Id = numeric value and exist in database
     * Expected: 200 OK with json response of customer's record
     * */
    @Test
    public void testGetCustomerRecord() throws Exception {
        mockMvc.perform(get("/1/customer/2")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.firstName").value(containsString(CUSTOMER_ID_2_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName").value(containsString(CUSTOMER_ID_2_LAST_NAME)));
    }



    /**
     * Test API - Create Customer
     * Expected: 200 OK with json response of customer's record
     * */
    @Test
    public void testCreateCustomerRecord_WithInvalidFirstName() throws Exception {
        Customer customer = new Customer(null, NEW_CUSTOMER_LAST_NAME);

        mockMvc.perform(post("/1/customer")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(customer)));
    }


    /**
     * Test API - Create Customer
     * Expected: 200 OK with json response of customer's record
     * */
    @Test
    public void testCreateCustomerRecord() throws Exception {
        Customer customer = new Customer(NEW_CUSTOMER_FIRST_NAME, NEW_CUSTOMER_LAST_NAME);

        mockMvc.perform(post("/1/customer")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(greaterThan(3)))
                .andExpect(jsonPath("$.firstName").value(containsString(NEW_CUSTOMER_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName").value(containsString(NEW_CUSTOMER_LAST_NAME)));
    }

    /**
     * Test API - Delete Customer
     * Input: Id = Empty String
     * Expected: NestedServletException with ConstraintException's message "pId: must not be blank"
     * */
    @Test
    public void testDeleteCustomerRecord_WhenUserIdIsEmptyString_ShouldThrowNestedServletException() {
        Throwable thrown = catchThrowable(() -> {
            mockMvc.perform(delete("/1/customer/ ").contentType(APPLICATION_JSON));
        });

        assertThat(thrown)
                .isInstanceOf(NestedServletException.class)
                .hasMessageContaining("pId: must not be blank");
    }

    /**
     * Test API - Delete Customer
     * Input: Id = non-numeric
     * Expected: 400 Bad Request with message "Invalid User Id"
     * */
    @Test
    public void testDeleteCustomerRecord_WithInvalidUserId() throws Exception {
        mockMvc.perform(delete("/1/customer/2a")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MSG_INVALID_USER_ID));
    }

    /**
     * Test API - Delete Customer
     * Input: Id = numeric value and exist in database
     * Expected: 202 Accepted with EMPTY response body
     * */
    @Test
    public void testDeleteCustomerRecord() throws Exception {
        assertTrue(mCustomerRepository.existsById(1L));

        mockMvc.perform(delete("/1/customer/1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().string(""));

        assertFalse(mCustomerRepository.existsById(1L));
    }

}
