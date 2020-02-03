package com.java.controller;

import com.java.entity.Customer;
import com.java.exception.CustomerNotFoundException;
import com.java.service.CustomerRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

;

@RestController
@RequestMapping("/customer")
@Validated
public class CustomerController {


	@Autowired
	CustomerRepoService customerRepoService;


	/**
	 * Get all customers
	 * @return List of Customers
	 */
	@GetMapping
	@ResponseBody
	public ResponseEntity<List<Customer>> getAllCustomers(){
		final List<Customer> customers = customerRepoService.getAllCustomers();
		if (CollectionUtils.isEmpty(customers)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(customers);
	}

	/**
	 * Get Customer by Id
	 * @param pId
	 * @return Customer
	 */
	@GetMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") @NotBlank String pId) {
		Customer customer = customerRepoService.getCustomer(Long.valueOf(pId));
		if(customer==null){ //can use Optional
			return ResponseEntity.notFound().build();
		}
		System.out.println(customer);
		return ResponseEntity.ok(customer);
	}

	/**
	 * Add Customer
	 * @param pCustomer
	 * @return Customer
	 */
	@PostMapping
	@ResponseBody
	public ResponseEntity<Customer> addCustomer(@RequestBody Customer pCustomer) {

		pCustomer.setId(0l);
		final Customer customer = customerRepoService.addCustomer(pCustomer);
		return ResponseEntity.ok(customer);
	}

	/**
	 * Update customer
	 * @param pId
	 * @return
	 */
	@PutMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<Customer> updateCustomer(@PathVariable("id") @NotBlank String pId, @RequestBody Customer pCustomer){
		try {
			customerRepoService.updateCustomer(Long.valueOf(pId), pCustomer);
		}catch(CustomerNotFoundException custNotFound){
			return ResponseEntity.notFound().build();
		}catch(Exception e){
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().build();
	}

	/**
	 * Delete customer
	 * @param pId
	 * @return
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<String> deleteCustomer(@PathVariable("id") @NotBlank String pId){

		Customer customer = customerRepoService.getCustomer(Long.valueOf(pId));

		// throw exception if null
		if (customer == null) {
			throw new CustomerNotFoundException("Customer id not found - " + pId);
		}
		customerRepoService.deleteById(Long.valueOf(pId));
		return ResponseEntity.ok().build();
	}


}
