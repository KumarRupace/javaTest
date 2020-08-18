package com.java.controller;

import com.java.dao.Customer;
import com.java.dao.CustomerRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

;

@RestController
@RequestMapping("/1/customer")
@Validated
public class CustomerController {

	@Autowired
	private CustomerRepoService customerRepoService;

	@GetMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") @NotBlank String pId) {
		Customer foundCustomer;
		try {
			foundCustomer = customerRepoService.getCustomerById(Long.parseLong(pId));
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (foundCustomer == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Customer>(foundCustomer, HttpStatus.OK);
	}

	@PutMapping(value = "/")
	@ResponseBody
	public ResponseEntity<Customer> addOrUpdateCustomer(@RequestBody Customer pCustomer) {
		Customer addOrUpdatedCustomer;
		try {
			addOrUpdatedCustomer = customerRepoService.addOrUpdateCustomer(pCustomer);
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(addOrUpdatedCustomer, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Long> deleteCustomer(@PathVariable("id") @NotBlank String pId) {
		long id = Long.parseLong(pId);

		if (customerRepoService.getCustomerById(id) == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		try {
			customerRepoService.deleteCustomer(id);
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(id, HttpStatus.OK);
	}

	@PostMapping(value = "/")
	public ResponseEntity<Customer> addCustomer(@RequestBody Customer pCustomer) {
		if (pCustomer.getId() != null) {
			if (customerRepoService.getCustomerById(pCustomer.getId()) != null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}

		Customer addedCustomer;

		try {
			addedCustomer = customerRepoService.addOrUpdateCustomer(pCustomer);
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(addedCustomer, HttpStatus.OK);
	}

}
