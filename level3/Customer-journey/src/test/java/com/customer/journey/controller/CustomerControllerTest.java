package com.customer.journey.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.customer.journey.domain.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class CustomerControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	 @Autowired
	 private ObjectMapper objectMapper;
	 
	 @Test
	 @Order(1) 
	 public void sanity_check_with_list_All_customers() throws Exception {	
			this.mockMvc.perform(get("/customer/listAllCustomers"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json"))			
					.andExpect(jsonPath("$[5].id", is(6)))
					.andExpect(jsonPath("$[5].firstName", is("Guoyao")))
					.andExpect(jsonPath("$[5].lastName", is("WEIMING")));
					
		}
		
	 @Test
	 @Order(2) 
	 public void sanity_check_with_find_by_customer_id_1() throws Exception {	
			this.mockMvc.perform(get("/customer/findById/3"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json"))
					.andExpect(jsonPath("$.id", is(3)))
					.andExpect(jsonPath("$.firstName", is("David")))
					.andExpect(jsonPath("$.lastName", is("JOHN")));
					
		}
		
	  @Test
	  @Order(3) 
	  public void sanity_check_with_find_by_customer_id_2() throws Exception {	
			this.mockMvc.perform(get("/customer/findById/8"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json"))
					.andExpect(jsonPath("$.id", is(8)))
					.andExpect(jsonPath("$.firstName", is("Kumar")))
					.andExpect(jsonPath("$.lastName", is("SENTHIL")));
					
		}	

	  @Test
	  @Order(4) 
	  public void sanity_check_with_create_customer_1() throws Exception {	
		
		Customer customer = new Customer("Dan","Wall");		
        String json = objectMapper.writeValueAsString(customer);
        mockMvc.perform(post("/customer/create")
        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .content(json).accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isCreated())
        		.andExpect(jsonPath("$.firstName", is("Dan")))
        		.andExpect(jsonPath("$.lastName", is("Wall")));
				
	  }
	
	   @Test
	   @Order(5) 
	   public void sanity_check_with_create_All_customers_1() throws Exception {	
			List<Customer> customers = new ArrayList<Customer>();
			customers.add( new Customer("Danny1","Walker1"));
			customers.add(new Customer("Danny2","Walker2"));
			customers.add(new Customer("Danny3","Walker3"));
			
	        String json = objectMapper.writeValueAsString(customers);
	        
	        mockMvc.perform(post("/customer/createAll")
        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .content(json).accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isCreated())
        		.andExpect(jsonPath("$[*]", hasSize(3)))
        		.andExpect(jsonPath("$[0].id", is(12)))
				.andExpect(jsonPath("$[0].firstName", is("Danny1")))
				.andExpect(jsonPath("$[0].lastName", is("Walker1")))
				.andExpect(jsonPath("$[2].firstName", is("Danny3")))
				.andExpect(jsonPath("$[2].lastName", is("Walker3")));
				
	   }
	   
	  
	   @Test
	   @Order(6) 
	   public void sanity_check_with_update_customer_1() throws Exception {	
			
			Customer customer = new Customer(Long.valueOf(2),"Tony","Greg");		
	        String json = objectMapper.writeValueAsString(customer);
	        mockMvc.perform(put("/customer/update")
	        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
	                .content(json).accept(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isCreated())
	        		.andExpect(jsonPath("$.id", is(2)))
	        		.andExpect(jsonPath("$.firstName", is("Tony")))
	        		.andExpect(jsonPath("$.lastName", is("Greg")));
					
		  }
	   
	   @Test
	   @Order(7) 
	   public void sanity_check_with_update_All_customer_1() throws Exception {	
			
		   List<Customer> customers = new ArrayList<Customer>();
			customers.add( new Customer(Long.valueOf(3),"Thomas1","Alexsandra1"));
			customers.add(new Customer(Long.valueOf(4),"Thomas2","Alexsandra2"));
			customers.add(new Customer(Long.valueOf(5),"Thomas3","Alexsandra3"));
			
	        String json = objectMapper.writeValueAsString(customers);
	        mockMvc.perform(put("/customer/updateAll")
	        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
	                .content(json).accept(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isCreated())
	        		.andExpect(jsonPath("$[*]", hasSize(3)))
	        		.andExpect(jsonPath("$[0].id", is(3)))
					.andExpect(jsonPath("$[0].firstName", is("Thomas1")))
					.andExpect(jsonPath("$[0].lastName", is("Alexsandra1")))
					.andExpect(jsonPath("$[2].firstName", is("Thomas3")))
					.andExpect(jsonPath("$[2].lastName", is("Alexsandra3")));
					
		  }
	   
	   @Test
	   @Order(8) 
	   public void sanity_check_with_delete_customer_1() throws Exception {	
			
			Customer customer = new Customer(Long.valueOf(3),"David","JOHN");		
	        String json = objectMapper.writeValueAsString(customer);
	        mockMvc.perform(delete("/customer/delete")
	        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
	                .content(json).accept(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isCreated())
	        		.andExpect(jsonPath("$.id", is(3)))
	        		.andExpect(jsonPath("$.firstName", is("David")))
	        		.andExpect(jsonPath("$.lastName", is("JOHN")));
					
		  }
	   
	   @Test
	   @Order(9) 
	   public void sanity_check_with_delete_All_customer_1() throws Exception {	
			
		   List<Customer> customers = new ArrayList<Customer>();
			customers.add( new Customer(Long.valueOf(4),"Junhao","XIANYONG"));
			customers.add(new Customer(Long.valueOf(5),"Lee","TAN"));			
			
	        String json = objectMapper.writeValueAsString(customers);
	        mockMvc.perform(delete("/customer/deleteAll")
	        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
	                .content(json).accept(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isCreated())
	        		.andExpect(jsonPath("$[*]", hasSize(2)))
	        		.andExpect(jsonPath("$[0].id", is(4)))
					.andExpect(jsonPath("$[0].firstName", is("Junhao")))
					.andExpect(jsonPath("$[0].lastName", is("XIANYONG")))
					.andExpect(jsonPath("$[1].firstName", is("Lee")))
					.andExpect(jsonPath("$[1].lastName", is("TAN")));
					
		  }
	   
	 
	   @Test
	   @Order(10) 
	   public void ctr_thrw_customer_not_found_by_id_1() throws Exception {
		   
		   this.mockMvc.perform(get("/customer/findById/30"))
			.andDo(print())
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.errMessage", is("Customer Not Found")))
			.andExpect(jsonPath("$.errUri", is("/customer/findById/30")));
		 }
	   
	   @Test
	   @Order(11) 
	   public void ctr_thrw_update_customer_failure_1() throws Exception {
		    Customer customer = new Customer(Long.valueOf(31),"Tony","Greg");		
	        String json = objectMapper.writeValueAsString(customer);
	        mockMvc.perform(put("/customer/update")
	        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
	                .content(json).accept(MediaType.APPLICATION_JSON))	        		
	        		.andExpect(jsonPath("$.errMessage", is("Can't perform update operation as 'Customer Not Found'")))
	    			.andExpect(jsonPath("$.errUri", is("/customer/update")));
		 }
	   
	   @Test
	   @Order(12) 
	   public void ctr_thrw_delete_customer_failure_1() throws Exception {
		    Customer customer = new Customer(Long.valueOf(31),"Tony","Greg");		
	        String json = objectMapper.writeValueAsString(customer);
	        mockMvc.perform(delete("/customer/delete")
	        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
	                .content(json).accept(MediaType.APPLICATION_JSON))	        
	        		.andExpect(jsonPath("$.errMessage", is("Can't perform delete operation as 'Customer Not Found'")))
	    			.andExpect(jsonPath("$.errUri", is("/customer/delete")));
		 }
	   
	   
	   @Test
	   @Order(13) 
	   public void thrw_constraint_for_invalid_customer_id_1() throws Exception {
			  
		   this.mockMvc.perform(get("/customer/findById/30ututuuryre"))
			.andDo(print())
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.errMessage", is("Give a valid customer id")))
			.andExpect(jsonPath("$.errUri", is("/customer/findById/30ututuuryre")));
		 
	    }
	   
	   @Test
	   @Order(14) 
	   public void thrw_constraint_for_invalid_customer_create_data_1() throws Exception {
		    Customer customer = new Customer("Tony","");		
	        String json = objectMapper.writeValueAsString(customer);
	        mockMvc.perform(post("/customer/create")
	        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
	                .content(json).accept(MediaType.APPLICATION_JSON))	        		
	        		.andExpect(jsonPath("$.errMessage", is("[Last Name Is Invalid]")))
	    			.andExpect(jsonPath("$.errUri", is("/customer/create")));
		 }
	   
	   @Test
	   @Order(15) 
	   public void thrw_constraint_for_invalid_customer_create_data_2() throws Exception {
		    Customer customer = new Customer("","");		
	        String json = objectMapper.writeValueAsString(customer);
	        mockMvc.perform(post("/customer/create")
	        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
	                .content(json).accept(MediaType.APPLICATION_JSON))	        		
	        		.andExpect(jsonPath("$.errMessage", is("[First Name Is Invalid, Last Name Is Invalid]")))
	    			.andExpect(jsonPath("$.errUri", is("/customer/create")));
		 }
	   
	   @Test
	   @Order(16) 
	   public void thrw_constraint_for_invalid_customer_create_data_3() throws Exception {
		    Customer customer = new Customer("","Bob");		
	        String json = objectMapper.writeValueAsString(customer);
	        mockMvc.perform(post("/customer/create")
	        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
	                .content(json).accept(MediaType.APPLICATION_JSON))	        		
	        		.andExpect(jsonPath("$.errMessage", is("[First Name Is Invalid]")))
	    			.andExpect(jsonPath("$.errUri", is("/customer/create")));
		 }
	   
	   @Test
	   @Order(17) 
	   public void thrw_constraint_for_invalid_customer_update_data_1() throws Exception {
		    Customer customer = new Customer("Tony","");		
	        String json = objectMapper.writeValueAsString(customer);
	        mockMvc.perform(put("/customer/update")
	        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
	                .content(json).accept(MediaType.APPLICATION_JSON))	        		
	        		.andExpect(jsonPath("$.errMessage", is("[Last Name Is Invalid]")))
	    			.andExpect(jsonPath("$.errUri", is("/customer/update")));
		 }
	   
	   @Test
	   @Order(18) 
	   public void thrw_constraint_for_invalid_customer_update_data_2() throws Exception {
		    Customer customer = new Customer("","");		
	        String json = objectMapper.writeValueAsString(customer);
	        mockMvc.perform(put("/customer/update")
	        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
	                .content(json).accept(MediaType.APPLICATION_JSON))	        		
	        		.andExpect(jsonPath("$.errMessage", is("[First Name Is Invalid, Last Name Is Invalid]")))
	    			.andExpect(jsonPath("$.errUri", is("/customer/update")));
		 }
	   
	   @Test
	   @Order(19) 
	   public void thrw_constraint_for_invalid_customer_update_data_3() throws Exception {
		    Customer customer = new Customer("","Bob");		
	        String json = objectMapper.writeValueAsString(customer);
	        mockMvc.perform(put("/customer/update")
	        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
	                .content(json).accept(MediaType.APPLICATION_JSON))	        		
	        		.andExpect(jsonPath("$.errMessage", is("[First Name Is Invalid]")))
	    			.andExpect(jsonPath("$.errUri", is("/customer/update")));
		 }
	   
	   @Test
	   @Order(20) 
	   public void thrw_constraint_for_invalid_customer_delete_data_1() throws Exception {
		    Customer customer = new Customer("Tony","");		
	        String json = objectMapper.writeValueAsString(customer);
	        mockMvc.perform(delete("/customer/delete")
	        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
	                .content(json).accept(MediaType.APPLICATION_JSON))	        		
	        		.andExpect(jsonPath("$.errMessage", is("[Last Name Is Invalid]")))
	    			.andExpect(jsonPath("$.errUri", is("/customer/delete")));
		 }
	   
	   @Test
	   @Order(21) 
	   public void thrw_constraint_for_invalid_customer_delete_data_2() throws Exception {
		    Customer customer = new Customer("","");		
	        String json = objectMapper.writeValueAsString(customer);
	        mockMvc.perform(delete("/customer/delete")
	        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
	                .content(json).accept(MediaType.APPLICATION_JSON))	        		
	        		.andExpect(jsonPath("$.errMessage", is("[First Name Is Invalid, Last Name Is Invalid]")))
	    			.andExpect(jsonPath("$.errUri", is("/customer/delete")));
		 }
	   
	   @Test
	   @Order(22) 
	   public void thrw_constraint_for_invalid_customer_delete_data_3() throws Exception {
		    Customer customer = new Customer("","bob");		
	        String json = objectMapper.writeValueAsString(customer);
	        mockMvc.perform(delete("/customer/delete")
	        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
	                .content(json).accept(MediaType.APPLICATION_JSON))	        		
	        		.andExpect(jsonPath("$.errMessage", is("[First Name Is Invalid]")))
	    			.andExpect(jsonPath("$.errUri", is("/customer/delete")));
		 }
	   
	   

}
