package com.dbstest.allclasses;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author Henry
 */
@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private CustomerRepoService service;
    @InjectMocks
    private CustomerController controller;

    private MockMvc mvc;
    private JacksonTester<Customer> custJson;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    public void When_createCustomer_Expect_ok() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.post("/customer").contentType(MediaType.APPLICATION_JSON)
                .content(custJson.write(new Customer("T7654321A", "Jason", Gender.MALE, 20000101))
                        .getJson())
        ).andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void When_updateCustomer_Expect_success() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.put("/customer").contentType(MediaType.APPLICATION_JSON)
                        .content(custJson.write(new Customer("T7654321A", "Jason", Gender.MALE, 20000101))
                                .getJson())
        ).andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void When_deleteCustomer_Expect_success() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.delete("/customer/T7654321A").accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void When_getCustomer_Expect_success() throws Exception {
        BDDMockito.given(service.getCustomer("T7654321A"))
                .willReturn(new Customer("T7654321A", "Jason", Gender.MALE, 20000101));

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.get("/customer/get/T7654321A").accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString())
                .isEqualTo(custJson.write((new Customer("T7654321A", "Jason", Gender.MALE, 20000101)))
                        .getJson());
    }

}