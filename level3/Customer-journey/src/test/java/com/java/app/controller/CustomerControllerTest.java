package com.java.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.dao.Customer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerTest {
    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private static final String USER = "user";
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void testGetCustomerWithNoAuthentication() throws Exception {
        mvc.perform(get("/1/customer/1"))
                .andExpect(status().is4xxClientError());
    }

    @WithMockUser(username=USER)
    @Test
    public void testGetCustomerWithExistingUser() throws Exception {
        mvc.perform(get("/1/customer/1"))
                .andDo(print())
                .andExpect(content().json("{\"id\":1,\"firstName\":\"David\",\"lastName\":\"Beckham\"}"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username=USER)
    @Test
    public void testGetCustomerWithNonExistingUser() throws Exception {
        mvc.perform(get("/1/customer/100"))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username=USER)
    @Test
    public void testWithNonNumericCustomerID() throws Exception {
        mvc.perform(get("/1/customer/100X"))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username=USER)
    @Test
    public void testWithCreateNewUser() throws Exception {
        final String lastName = UUID.randomUUID().toString();
        final String firstName = UUID.randomUUID().toString();
        mvc.perform(get("/1/customer/lastName/" + lastName))
                .andExpect(status().isNoContent());

        mvc.perform(post("/1/customer/").contentType(APPLICATION_JSON_UTF8)
                .content(asJsonString(new Customer(firstName, lastName)))
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

        mvc.perform(get("/1/customer/lastName/" + lastName))
                .andExpect(status().isOk());

    }

    @WithMockUser(username=USER)
    @Test
    public void testWithCreateOrUpdateUser() throws Exception {
        final String lastName = UUID.randomUUID().toString();
        final String firstName = UUID.randomUUID().toString();
        mvc.perform(get("/1/customer/lastName/" + lastName))
                .andExpect(status().isNoContent());

        mvc.perform(put("/1/customer/").contentType(APPLICATION_JSON_UTF8)
                .content(asJsonString(new Customer(firstName, lastName)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        mvc.perform(get("/1/customer/lastName/" + lastName))
                .andExpect(status().isOk());
    }

    @WithMockUser(username=USER)
    @Test
    public void testWithDeleteNonExistingUser() throws Exception {
        mvc.perform(get("/1/customer/400"))
                .andExpect(status().isNoContent());
        mvc.perform(delete("/1/customer/400"))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username=USER)
    @Test
    public void testWithDeleteExistingUser() throws Exception {
        mvc.perform(get("/1/customer/4"))
                .andExpect(content().json("{\"id\":4,\"firstName\":\"Adam\",\"lastName\":\"Smith\"}"))
                .andExpect(status().isOk());
        mvc.perform(delete("/1/customer/4"))
                .andExpect(status().isOk());
        mvc.perform(get("/1/customer/4"))
                .andExpect(status().isNoContent());
    }


    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
