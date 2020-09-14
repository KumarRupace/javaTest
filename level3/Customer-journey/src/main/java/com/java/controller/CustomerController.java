package com.java.controller;

import com.java.dao.Customer;
import com.java.dao.CustomerRepoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/1/customer")
@Validated
public class CustomerController {

	@Autowired
	private CustomerRepoService customerRepoService;

	@GetMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") @NotBlank String pId) {
		log.info("getCustomer by ID: {}", pId);
		long id = Long.parseLong(pId);
		Customer customer = customerRepoService.getCustomerById(id);
		if (customer == null) {
			log.info("no customer was found with ID {}", pId);
			return ResponseEntity.noContent().build();
		}
		log.info("Customer was found : {}", customer.toString());
		return ResponseEntity.ok(customer);
	}

	@GetMapping(value = "/lastName/{lastName}")
	@ResponseBody
	public ResponseEntity<List<Customer>> getCustomerByLastName(@PathVariable("lastName") @NotBlank String lastName) {
		log.info("getCustomer by lastName: {}", lastName);

		List<Customer> customers = customerRepoService.findCustomersByLastName(lastName);
		if (CollectionUtils.isEmpty(customers)) {
			log.info("no customer was found with lastName {}", lastName);
			return ResponseEntity.noContent().build();
		}
		log.info("Customer was found : {}", customers.toString());
		return ResponseEntity.ok(customers);
	}

	@PostMapping("/")
	@ResponseBody
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer newCustomer) {
		log.info("createCustomer with : {}", newCustomer);
		Customer customer = customerRepoService.createCustomerByFirstLastName(newCustomer.getFirstName(), newCustomer.getLastName());
		log.info("Customer was created : {}", customer.toString());

		return ResponseEntity.ok(customer);
	}

	@PutMapping(value = "/")
	@ResponseBody
	public ResponseEntity<Customer> addOrUpdateCustomer(@RequestBody Customer pCustomer) {
		Customer customer = customerRepoService.updateOrCreateCustomer(pCustomer);
		return ResponseEntity.ok(customer);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> deleteCustomerById(@PathVariable("id") String pId) {
		log.info("deleteCustomerById: {}", pId);
		long id = Long.parseLong(pId);
		Customer customer = customerRepoService.getCustomerById(id);
		if (customer == null) {
			log.info("no customer was found with ID {}", pId);
			return ResponseEntity.noContent().build();
		}

		customerRepoService.deleteCustomerById(id);
		return ResponseEntity.ok("Customer ID " + id + " is removed");
	}

	@ExceptionHandler({ConstraintViolationException.class, NumberFormatException.class})
	ResponseEntity<String> handleConstraintViolationException(Exception e) {
		return ResponseEntity.badRequest().body("Not valid due to validation error: " + e.getMessage());
	}

}
