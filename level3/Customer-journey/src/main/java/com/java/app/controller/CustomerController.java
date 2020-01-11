package com.java.app.controller;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.java.app.dao.CustomerRepoService;
import com.java.app.domain.Customer;;

/**
 * @author prasanna kumar
 *
 */
@RestController
@RequestMapping("/customercontroller")
@Validated
@EnableAutoConfiguration
public class CustomerController {

	@Autowired
	private CustomerRepoService repository;
	
	/**
	 * @param customer
	 * @return ResponseEntity<Customer>
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<Customer> saveCustomer(@RequestBody  Customer customer) {
		Customer cust = repository.save(customer);
		return new ResponseEntity<Customer>(cust, HttpStatus.OK);
	}
	

	/**
	 * @param pId
	 * @return ResponseEntity<Customer>
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") @NotBlank String pId) {
		Customer cust = repository.findById(Long.parseLong(pId));
		return new ResponseEntity<Customer>(cust, HttpStatus.OK);
	}
	
	/**
	 * @param customer
	 * @return ResponseEntity<Customer>
	 */
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) {
		Customer cust = repository.update(customer);
		return new ResponseEntity<Customer>(cust, HttpStatus.OK);
	}
	
	/**
	 * @param pId
	 * @return ResponseEntity<String>
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteCustomer(@PathVariable("id") @NotBlank String pId) {
		 String status= repository.delete(Long.parseLong(pId));
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}
	
}
