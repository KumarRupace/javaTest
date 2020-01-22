package com.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.java.dao.CustomerRepository;
import com.java.dao.Customer;
import com.java.dao.CustomerRepoService;

import org.junit.Assert;
import org.junit.Test;

//Author: Zhang Xin

@SpringBootApplication
public class CustomerRepoServiceTest {

	private CustomerRepoService crs = new CustomerRepoService();

	public static void main(String[] args) {
		SpringApplication.run(CustomerRepoServiceTest.class, args);
	}
  
	@Bean
	public CommandLineRunner demo(CustomerRepository repository) {
		return (args) -> {			
			crs.setCustomerRepository(repository);

			/*Create four customers. Reject the one without nric.*/
			createCustomers("S8080808Z","JACK", "MA", 98765432);
			createCustomers("S7070707Z","BILL", "GATES", 97654321);
			createCustomers("S6060606Z","MARK", "ZUCKERBERG", 96543219);
			createCustomers("","MARK", "ZUCKERBERG", 96543219);

			countCustomers();
			
			/*Retrieve all records*/
			retrieveCustomers();
			
			/*Retrieve by ID. Reject the one without ID.*/
			retrieveCustomerById(1L);
			retrieveCustomerById(0);
			
			
			/*Retrieve customer with nric. Reject the one without nric*/
			retrieveCustomerByNric("S8080808Z");
			retrieveCustomerByNric("");
			
			/*Retrieve customer by last name*/
			retrieveByLastName("ZUCKERBERG");
			
			/*Retrieve customer by first name*/
			retrieveByFirstName("JACK");
			
			/*Delete all customers*/
			deleteAllCustomers();
			
			countCustomers();
		};
	}
	
	/*Test cases in positive and negative (POST, PUT and DELETE) were provided in the same method.
	  All assert error were handle to allow program to proceed on.	  
	*/ 
	@Test
	private void createCustomers(String nric, String firstName, String lastName, long number){
		try{
			Assert.assertNotEquals("",nric);		        		
			Assert.assertNotEquals("",firstName);	
			Assert.assertNotEquals("",lastName);	
			Assert.assertNotEquals(0,number);	
			/*Create 3 customers in DB*/
			crs.customerStore(new Customer(nric, firstName, lastName, number));
			System.out.println();
			System.out.println();
			System.out.println();
		}
		catch(AssertionError e){
			//Handle assertion error						
			System.out.println(e.getMessage());		
		}
	}
	@Test
	private void retrieveCustomers(){
		//Retrieve all customers in DB
		crs.customerRetrieveAll();
		System.out.println();
		System.out.println();
		System.out.println();
	}
	@Test
	private void retrieveCustomerById(long id){
		try{
			Assert.assertNotEquals(0,id);		
			//Retrieve by ID
			crs.customerRetrieveById(id);
			System.out.println();
			System.out.println();
			System.out.println();
		}catch(AssertionError e){
			//Handle assertion error					
			System.out.println(e.getMessage());		
		}
	}
	@Test
	private void retrieveCustomerByNric(String nric){
		try{
			Assert.assertNotEquals("",nric);		
			//Retrieve by NRIC
			crs.customerRetrieveByNric("S8080808Z");
			System.out.println();
			System.out.println();
			System.out.println();
		}catch(AssertionError e){
			//Handle assertion error					
			System.out.println(e.getMessage());		
		}
	}
	@Test
	private void retrieveByLastName(String lastName){
		try{
			Assert.assertNotEquals("",lastName);
			//Retrieve by last name
			crs.customerRetrieveByLastName(lastName);		  
			System.out.println();
			System.out.println();
			System.out.println();
		  }catch(AssertionError e){
			//Handle assertion error					
			System.out.println(e.getMessage());		
		}
	}
	@Test
	private void retrieveByFirstName(String firstName){
		try{
			Assert.assertNotEquals("",firstName);
			//Retrieve by first name
			crs.customerRetrieveByFirstName(firstName);	  
			System.out.println();
			System.out.println();
			System.out.println();
		}catch(AssertionError e){
			//Handle assertion error
			System.out.println(e.getMessage());		
		}
	}
	@Test
	private void deleteAllCustomers(){
		//Delete all
		crs.customerDeleteAll();
		System.out.println();
		System.out.println();
		System.out.println();
	}
	@Test
	private void countCustomers(){
		//Count all customers in DB		  
		crs.customerCount();
	}
}
