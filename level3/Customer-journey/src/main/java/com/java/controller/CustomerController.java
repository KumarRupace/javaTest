package com.java.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.java.dao.Customer;
import com.java.dao.CustomerRepoService;
import com.java.dao.CustomerRepository;

//Author: Zhang Xin

@RestController
@RequestMapping("/customer")
public class CustomerController{

	private static CustomerRepository repository;
	private CustomerRepoService crs = new CustomerRepoService();
	 
	public void setCustomerRepository(CustomerRepository repository){
		this.repository = repository;	
	}
	
	/*	Create a new customer
		1) Check for existing customer based on NRIC
		2) Reject creation if customer existed based on NRIC
	*/
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody Customer c, UriComponentsBuilder ucBuilder) {
		Customer customerCreate ;
		
		try{
		//Check whether customer exist
		if(null != c){
			if( null != c.getNric()){			
				customerCreate = checkExistingCustomer(c.getNric());	
				
				//Customer does not exist, proceed with creation
				if(null == customerCreate){				
					crs.customerStore(c);
				}else{//Customer exist, abort creation
					return new ResponseEntity("Unable to create because customer with "+c.getNric()+" already existed.",HttpStatus.CONFLICT);
				}			
			}			
		}
 		
		//Display all records in DB for clarity
		crs.customerRetrieveAll();
 
		return new ResponseEntity<>("Created customer with NRIC "+ c.getNric(), HttpStatus.CREATED);
		
		}catch(Exception e){			
			return new ResponseEntity("Issue occurred during creation. Abort creation.",HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
 
    /*	Update customer
		1) Check for existing customer based on NRIC
		2) Update if customer existed based on NRIC
		3) Reject if no customer found based on NRIC
	*/ 
    @RequestMapping(value = "/{nric}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable("nric") String nric, @RequestBody Customer c) {
        
		try{
			//Check whether customer exist
			Customer customerUpdate = checkExistingCustomer(nric);
	 
			//Customer does not exist, abort update operation
			if (null == customerUpdate) {
				System.out.println();
				return new ResponseEntity<String>("Unable to update because customer with "+c.getNric()+" was not found.", HttpStatus.CONFLICT);
			}
			 
			//Customer existed, append with new customer's information
			customerUpdate.setNric(c.getNric());
			customerUpdate.setFirstName(c.getFirstName());
			customerUpdate.setLastName(c.getLastName());
			customerUpdate.setNumber(c.getNumber());
			
			crs.customerUpdate(customerUpdate);
			
			//Display all records in DB for clarity
			crs.customerRetrieveAll();
			
			return new ResponseEntity<>("Customer details updated: "+customerUpdate.getNric(), HttpStatus.OK);
		}catch(Exception e){			
			return new ResponseEntity<>("Issue occurred during update. Abort update.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
 
    /*	Delete customer
		1) Check for existing customer based on NRIC
		2) Delete if customer existed based on NRIC
		3) Reject if no customer found based on NRIC
	*/ 
    @RequestMapping(value = "/{nric}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("nric") String nric) {
      
		try{
			//Check whether customer exist	  
			Customer customerDelete = checkExistingCustomer(nric);
			
			//Customer does not exist, abort delete operation
			if (null == customerDelete) {
				return new ResponseEntity<>("Unable to delete as customer with "+nric+ " was not found.", HttpStatus.CONFLICT);
			}
			
			//Customer exist, proceed with delete operation
			crs.customerDelete(customerDelete);
			
			//Display all records in DB for clarity
			crs.customerRetrieveAll();
			
			return new ResponseEntity<>("Customer with "+nric+ " deleted.", HttpStatus.OK);
		}
		catch(Exception e){			
			return new ResponseEntity("Issue occurred during deletion. Abort deletion.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
 
	private Customer checkExistingCustomer(String nric){
		Customer customer = crs.customerRetrieveByNric(nric);
		return customer;
	}
}
