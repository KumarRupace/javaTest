package com.customer.journey;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.customer.journey.controller.CustomerController;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class CustomerJourneyApplicationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private CustomerController controller;	
	
	@Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

	@Test
	@Order(1) 
	public void sanity_check_with_default_initialized_data_1() throws Exception {
		this.mockMvc.perform(get("/customer/listAllCustomers"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("God")))
				.andExpect(content().string(containsString("David")))
				.andExpect(content().contentType("application/json"));
				
	}
	
	@Test
	@Order(2) 
	public void sanity_check_with_default_initialized_data_2() throws Exception {	
		this.mockMvc.perform(get("/customer/listAllCustomers"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$[*]", hasSize(10)))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].firstName", is("Shiva")))
				.andExpect(jsonPath("$[0].lastName", is("MURUGAN")));
				
	}
	
	@Test
    @Order(3) 
	public void sanity_check_with_default_initialized_data_3() throws Exception {	
		this.mockMvc.perform(get("/customer/listAllCustomers"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$[*]", hasSize(10)))
				.andExpect(jsonPath("$[9].id", is(10)))
				.andExpect(jsonPath("$[9].firstName", is("God")))
				.andExpect(jsonPath("$[9].lastName", is("WIN")));
				
	}
	
	@Test
	@Order(4) 
	public void sanity_check_with_default_initialized_data_4() throws Exception {	
		this.mockMvc.perform(get("/customer/listAllCustomers"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$[*]", hasSize(10)))
				.andExpect(jsonPath("$[5].id", is(6)))
				.andExpect(jsonPath("$[5].firstName", is("Guoyao")))
				.andExpect(jsonPath("$[5].lastName", is("WEIMING")));
				
	}
	
	@Test
	@Order(5) 
	public void sanity_check_with_default_initialized_data_5() throws Exception {	
		this.mockMvc.perform(get("/customer/findById/3"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.id", is(3)))
				.andExpect(jsonPath("$.firstName", is("David")))
				.andExpect(jsonPath("$.lastName", is("JOHN")));
				
	}
	
	@Test
	@Order(6) 
	public void sanity_check_with_default_initialized_data_6() throws Exception {	
		this.mockMvc.perform(get("/customer/findById/8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.id", is(8)))
				.andExpect(jsonPath("$.firstName", is("Kumar")))
				.andExpect(jsonPath("$.lastName", is("SENTHIL")));
				
	}	
	
}
