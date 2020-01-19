package com.java.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.dao.Customer;
import com.java.dao.CustomerRepoService;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerControllerTest {

	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private CustomerRepoService customerRepoService;

	private MockMvc mockMvc;
	
	private ObjectMapper objectMapper;
	
	private final String URL_PREFIX = "/1/customer";
	
	private Customer customerDto = null;
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		
		objectMapper = new ObjectMapper();
		
		this.customerDto = new Customer();
		this.customerDto.setFirstName("kimi 123124");
		this.customerDto.setLastName("qian 123213");
	}

	@Test
	public void when1AddOrUpdateCustomerSuccess() throws Exception {
		
		String jsonBodyString = objectMapper.writeValueAsString(this.customerDto);
		
		String result = mockMvc.perform(put(URL_PREFIX + "/")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(jsonBodyString))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.firstName").value(this.customerDto.getFirstName()))
				.andExpect(jsonPath("$.lastName").value(this.customerDto.getLastName()))
				.andReturn()
				.getResponse()
				.getContentAsString();
		
		System.out.format("result - {}", result);
	}
	
	@Test
	public void when2GetCustomerSuccess() throws Exception {
		Customer customer = customerRepoService.addOrUpdateCustomer(this.customerDto);
		
		mockMvc.perform(get(URL_PREFIX + "/" + customer.getId())
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.id").value(customer.getId()));
	}
	
	@Test
	public void when3GetCustomerByLastNameSuccess() throws Exception {
		mockMvc.perform(get(URL_PREFIX)
				.param("lastName", this.customerDto.getLastName())
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$[0].lastName").value(this.customerDto.getLastName()));
	}
	
	@Test
	public void when4DeleteSuccess() throws Exception {
		Customer customer = customerRepoService.addOrUpdateCustomer(this.customerDto);
		
		mockMvc.perform(delete(URL_PREFIX + "/" + customer.getId())
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().string("true"));
	}
	
	@Test
	public void when5AddOrUpdateCustomer_whenCusotmerLastNameIsEmpty_thenResponseStatusIs400() throws Exception {
		Customer customerDto = new Customer(RandomStringUtils.random(2), null);
		
		String jsonBodyString = objectMapper.writeValueAsString(customerDto);
		
		mockMvc.perform(put(URL_PREFIX + "/")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(jsonBodyString))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void when6AddOrUpdateCustomer_whenCusotmerLastNameIsEmpty_whenCustomerFirstNameIsEmpty_thenResponseStatusIs400() throws Exception {
		Customer customerDto = new Customer();
		
		String jsonBodyString = objectMapper.writeValueAsString(customerDto);
		
		mockMvc.perform(put(URL_PREFIX + "/")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(jsonBodyString))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void when7AddOrUpdateCustomer_whenCusotmerLastNameLengthIs5_thenResponseStatusIs400() throws Exception {
		Customer customerDto = new Customer(RandomStringUtils.random(2), RandomStringUtils.random(5));
		
		String jsonBodyString = objectMapper.writeValueAsString(customerDto);
		
		mockMvc.perform(put(URL_PREFIX + "/")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(jsonBodyString))
				.andExpect(status().isBadRequest());
	}
	
	@Test(expected = NestedServletException.class)
	public void when8DeleteCustomer_whenCusotmerNotExisting_thenResponseStatusis500() throws Exception {
		mockMvc.perform(delete(URL_PREFIX + "/" + new Random().nextInt(100) * 1000)
				.contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void when9GetCustomerByLastName_whenReturnIsEmpty_thenReturnIsEmptyArray() throws Exception {
		mockMvc.perform(get(URL_PREFIX)
				.param("lastName", RandomStringUtils.random(6))
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().string("[]"));
	}
	
}
