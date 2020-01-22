package com.java;
 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication; 
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.java.dao.CustomerRepository;
import com.java.dao.CustomerRepoService;
import com.java.dao.Customer;
import java.net.URI;  

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.HashMap;
import java.util.Map;

//Author: Zhang Xin

@SpringBootApplication(scanBasePackages={"com.java"})
public class CustomerControllerTest {
   
	private CustomerRepoService crs = new CustomerRepoService();
	private static final RestTemplate restTemplate = new RestTemplate();
	private static final String REST_SERVICE_URI = "http:/"+"/localhost:8080/"+"/customer/";	
	
	public static void main(String[] args) {
		SpringApplication.run(CustomerControllerTest.class, args);
	}
  
	@Bean
	public CommandLineRunner demo(CustomerRepository repository) {
		return (args) -> {
			crs.setCustomerRepository(repository);
			
			/*Create 4 customers (including 1 duplicate). Duplicate shall be rejected.*/
			System.out.println("########## CREATING USER ##########");
			createUser("S8080808Z","JACK","MA", 98765432);	
			createUser("S7070707Z","BILL","GATES", 97654321);	
			createUser("S6060606Z","MARK","ZUCKERBERG", 96543219);	
			createUser("S6060606Z","MARK","ZUCKERBERG", 96543219);	
			System.out.println();		  
			System.out.println();
		  	System.out.println();		  
			System.out.println();
			
			/*Update 3 customers. One does not exist and one missing NRIC in DB. Shall be rejected*/
			System.out.println("########## UPDATE USER ##########");
			updateUser("S8080808Z","JACK","TAN", 98765432);	
			updateUser("S5050505Z","STEVE","JOB", 88765432);	
			updateUser("","MARK","ZUCKERBERG", 68765432);	
			System.out.println();		  
			System.out.println();
			System.out.println();		  
			System.out.println();
			
			/*Delete customers, exclude one missing NRIC.*/
			System.out.println("########## DELETE USER ##########");
			deleteUser("S8080808Z");		  
			deleteUser("");
			System.out.println();		  
			System.out.println();
			System.out.println();		  
			System.out.println();
		};
	}
	/*Test cases in positive and negative (POST, PUT and DELETE) were provided in the same method.
	  All assert error were handled to allow program to proceed on.	  
	*/     
	
    /* Creation of customer using POST */
	@Test
    private void createUser(String nric, String firstName, String lastName, long number) {  
		ResponseEntity response =null;
		
		try{
			Assert.assertNotEquals("",nric);		        		
			Assert.assertNotEquals("",firstName);	
			Assert.assertNotEquals("",lastName);	
			Assert.assertNotEquals(0,number);	
		
			Customer custCreate = new Customer(nric ,firstName, lastName, number);		
			response = restTemplate.postForEntity(REST_SERVICE_URI, custCreate, String.class);
				
			//Verify request succeed
			Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());		
		}catch(AssertionError e){
			//Verify assertion error case						
			System.out.println(e.getMessage());			
		}catch(Exception e){
			System.out.println(e.getMessage());			
		}
		
		log(response);
    }
  
    /* Update of customer's information using PUT */
    @Test
	private static void updateUser(String nric, String firstName, String lastName, long number) {
			ResponseEntity response =null;      
	  try{
			Assert.assertNotEquals("",nric);		        		
			Assert.assertNotEquals("",firstName);	
			Assert.assertNotEquals("",lastName);	
			Assert.assertNotEquals(0,number);	
			
			Customer custUpdate  = new Customer(nric, firstName, lastName, number);
			Assert.assertNotNull(custUpdate);		        
			HttpEntity<Customer> entity = new HttpEntity<Customer>(custUpdate);
      
			response = restTemplate.exchange(REST_SERVICE_URI+"/"+nric, HttpMethod.PUT, entity, String.class);
			//Verify request succeed
			Assert.assertEquals(HttpStatus.OK, response.getStatusCode());	
			//restTemplate.put(REST_SERVICE_URI+"/"+nric, custUpdate);        
		}catch(AssertionError e){
			System.out.println(e.getMessage());
		}catch(Exception e){
			System.out.println(e.getMessage());			
		}
		
		log(response);
    }
  
    /* Deletion of customer's information using DELETE */
    private static void deleteUser(String nric) {
		ResponseEntity response =null; 
		try{
			Assert.assertNotEquals("",nric);		        					
			//restTemplate.delete(REST_SERVICE_URI+"/"+nric);
			
			HttpHeaders headers = new HttpHeaders();
			Assert.assertNotEquals("",nric);		        
			HttpEntity<String> entity = new HttpEntity<String>(nric);
      
			response = restTemplate.exchange(REST_SERVICE_URI+"/"+nric, HttpMethod.DELETE, entity, String.class);
			Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		}catch(AssertionError e){
			System.out.println(e.getMessage());
		}catch(Exception e){
			System.out.println(e.getMessage());			
		}
		log(response);
    }  
	
	private static void log(ResponseEntity response){
		if(null != response){
			System.out.println(response.getBody());		
		}			
	}
	
}