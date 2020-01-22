package com.java.dao;

import org.springframework.beans.factory.annotation.Autowired;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import com.java.dao.CustomerRepository;
import com.java.dao.Customer;

//Author: Zhang Xin

/*All customers shall be retrieve based on unique identifier, which is their NRIC*/
public class CustomerRepoService{

	@Autowired
	private static CustomerRepository repository;

	public void setCustomerRepository(CustomerRepository repository){
		this.repository = repository;
	}
	//Store customer information
	public void customerStore(Customer c){
		repository.save(c);		
	}
	
	//Update customer information
	public void customerUpdate(Customer c){
		repository.save(c);
	}
	
	//Delete customer information
	public void customerDelete(Customer c){
		repository.delete(c);
	}
	
	//Retrieve customer by ID
	public void customerRetrieveById(long id){
		//Retrieve customer by ID
		  Customer cusId = repository.findById(id);
		  System.out.println("---------------------------------");
		  System.out.println("Retrieve by ID				   ");
		  System.out.println("---------------------------------");
		  System.out.println(cusId.toString());
		 
	}	

	//Retrieve customer by NRIC
	public Customer customerRetrieveByNric(String nric){
		System.out.println("");
		  System.out.println("---------------------------------");
		  System.out.println("Retrieve by NRIC	 			   ");
		  System.out.println("---------------------------------");
		  
		  Customer cusNric = null;
		  try{
			cusNric = repository.findByNric(nric);
			
		  if(null != cusNric){
			System.out.println("Customer exist: "+cusNric.toString());  
		  }else{
			System.out.println("Customer with "+nric+" does not exist");  
		  }
		  }catch(Exception e){
			  System.out.println("Customer with "+nric+" does not exist");  
		  }
		 
		  return cusNric;
	}	
		
	//Retrieve customer by last name
	public void customerRetrieveByLastName(String lastName){
		//Retrieve customers by last name
		System.out.println("---------------------------------");
		System.out.println("Retrieve by last name			 ");
		System.out.println("---------------------------------");
        repository.findByLastName(lastName).forEach(lname -> {
			System.out.println(lname.toString());
		});		
	}		

	//Retrieve customer by last name
	public void customerRetrieveByFirstName(String firstName){
		//Retrieve customers by first name
		System.out.println("---------------------------------");
		System.out.println("Retrieve by first name			 ");
		System.out.println("---------------------------------");
        repository.findByFirstName(firstName).forEach(fname -> {
			System.out.println(fname.toString());
		});
		
	}	

	//List out all customers in DB
	public void customerRetrieveAll(){
		System.out.println("-------------------------------");
		System.out.println("All customers                ");
		System.out.println("-------------------------------");
		  
		for (Customer customer : repository.findAll()) {
			System.out.println(customer.toString());
		}	
	}	
	
	//Delete all customers in DB
	public void customerDeleteAll(){
		System.out.println("-------------------------------");
		System.out.println("Delete all customers                ");
		System.out.println("-------------------------------");
		repository.deleteAll();
	}
	
	public void customerCount(){
		System.out.println("Total number of customer(s): "+repository.count());
	}
	
	
}
