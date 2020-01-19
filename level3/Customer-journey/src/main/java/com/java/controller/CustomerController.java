package com.java.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.java.dao.Customer;
import com.java.dao.CustomerRepoService;;

/**
 * Customer controller to handle request
 * 
 * @author Kimi Qian Min
 *
 */
@RestController
@RequestMapping("/1/customer")
@Validated
public class CustomerController {

	@Autowired
	private CustomerRepoService customerService;

	/**
	 * Request path "/" handler
	 * 
	 * @param customerDto
	 */
	@PutMapping(value = "/")
	@ResponseBody
	public ResponseEntity<Customer> addOrUpdateCustomer(@Valid @RequestBody Customer customerDto) {
		return new ResponseEntity<>(this.customerService.addOrUpdateCustomer(customerDto), HttpStatus.OK);
	}

	/**
	 * Request path "/{id}" handler
	 * 
	 * @param id
	 */
	@GetMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") @NonNull Long id) {
		return new ResponseEntity<>(this.customerService.getCustomer(id), HttpStatus.OK);
	}

	/**
	 * Request path "" handler
	 * 
	 * @param lastName
	 */
	@GetMapping(value = "")
	@ResponseBody
	public ResponseEntity<List<Customer>> getCustomerByLastName(@RequestParam("lastName") @NonNull String lastName) {
		return new ResponseEntity<>(this.customerService.getCustomerByLastName(lastName), HttpStatus.OK);
	}

	/**
	 * Request path {id} handler
	 * 
	 * @param id
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<Boolean> deleteCustomer(@PathVariable("id") @NonNull Long id) {
		return new ResponseEntity<>(this.customerService.deleteCustomer(id), HttpStatus.OK);
	}

}
