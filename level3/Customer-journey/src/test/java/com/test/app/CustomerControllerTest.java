package com.test.app;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.java.app.AccessingDataJpaApplication;
import com.java.app.controller.CustomerController;
import com.java.app.dao.CustomerRepoService;
import com.java.app.domain.Customer;

/**
 * @author prasanna kumar
 *
 */

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AccessingDataJpaApplication.class)
@WebMvcTest(value = CustomerController.class)
public class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private CustomerRepoService customerService;
	
	/**
	 * @throws Exception 
	 * 
	 */
	@Test
	public void getCustomerbyId() throws Exception {
		
		Customer customer = new Customer((long)1,"kumar","bala");
		Mockito.when(customerService.findById(Long.valueOf(1))).thenReturn(customer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/customercontroller/1").accept(
				MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "{ id:1, firstName : kumar ,lastName : bala}";
		
		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);

	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void saveCustomer() throws Exception{
		
		String samplecust = "{\"id\":3,\"firstName\":\"venky\",\"lastName\":\"indu\"}";
		Customer customer = new Customer((long)1,"venky","indu");
		
		Mockito.when(customerService.save(customer)).thenReturn(customer);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
				"/customercontroller/save") 
				.contentType(MediaType.APPLICATION_JSON).content(samplecust);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(200, response.getStatus());
	}
	
	
	/**
	 * @throws Exception
	 */
	@Test
	public void updateCustomer() throws Exception{
		
		String samplecust = "{\"firstName\":\"venky\",\"lastName\":\"indu\"}";
		Customer customer = new Customer((long)1,"bala","indu");
		
		Mockito.when(customerService.save(customer)).thenReturn(customer);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
				"/customercontroller/update") 
				.contentType(MediaType.APPLICATION_JSON).content(samplecust);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(200, response.getStatus());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void deleteCustomerbyId() throws Exception {
		
		Customer customer = new Customer((long)1,"kumar","bala");
		Mockito.when(customerService.findById(Long.valueOf(1))).thenReturn(customer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(
				"/customercontroller/delete/1").accept(
				MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
	    Mockito.verify(customerService, times(1)).delete((long)1);

		}
	
}
