package com.java.controller;

import com.java.dao.Customer;
import com.java.dao.CustomerRepoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/1/customer")
@Validated
public class CustomerController {

	@Autowired
	private CustomerRepoService service;

	@ApiOperation(notes = "Returns a Customer by id", value = "Returns a Customer by id", tags = "Customer")
	@GetMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") @NotBlank String pId) {
		return new ResponseEntity<>(service.findCustomerById(pId), HttpStatus.OK);
	}

	@ApiOperation(notes = "Deleted a Customer by id", value = "Deletes a Customer by id", tags = "Customer")
	@DeleteMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<String> deleteCustomer(@PathVariable String id) {
		service.deleteCustomer(id);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);

	}

	@ApiOperation(notes = "Add or Update a Customer", value = "Add or Update a Customer", tags = "Customer")
	@PutMapping(value = "/updateCustomer")
	@ResponseBody
	public ResponseEntity<Customer> addOrUpdateCustomer(@RequestBody Customer customer) {
		return new ResponseEntity<>(service.updateOrAddCustomer(customer), HttpStatus.OK);
	}
}
