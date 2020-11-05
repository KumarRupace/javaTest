package com.java.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.java.dao.CustomerRepoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.java.dao.Customer;

import static com.java.app.Constants.MSG_INVALID_USER_ID;
import static com.java.app.Constants.MSG_NO_RECORD_FOUND;

@Validated
@RestController
@RequestMapping("/1/customer")
public class CustomerController {
	
	@Autowired
	private CustomerRepoService mCustomerService;

	@GetMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<?> getCustomer(@PathVariable("id") @NotBlank String pId) {
		try {
			long id = Long.parseLong(pId);

			Customer customer = mCustomerService.retrieve(id);

			if (customer != null) {
				return ResponseEntity.ok(customer);
			}

			return ResponseEntity.ok().body(MSG_NO_RECORD_FOUND);
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().body(MSG_INVALID_USER_ID);
		}
	}

	@PostMapping
	@ResponseBody
	public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer pCustomer) {
		if (pCustomer.getId() != null) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.created(null).body(mCustomerService.save(pCustomer));
	}

	@PutMapping
	@ResponseBody
	public ResponseEntity<Customer> updateCustomer(@Valid @RequestBody Customer pCustomer) {
		if (pCustomer == null || pCustomer.getId() == null) {
			return ResponseEntity.badRequest().build();
		}

		if (mCustomerService.exists(pCustomer.getId())) {
			return ResponseEntity.accepted().body(mCustomerService.save(pCustomer));
		}

		return ResponseEntity.badRequest().build();
	}

	@DeleteMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<?> deleteCustomer(@PathVariable("id") @NotBlank String pId) {
		try {
			long id = Long.parseLong(pId);

			mCustomerService.delete(id);

			return ResponseEntity.accepted().build();
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().body(MSG_INVALID_USER_ID);
		}
	}

}
