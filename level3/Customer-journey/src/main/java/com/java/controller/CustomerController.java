package com.java.controller;

import javax.validation.constraints.NotBlank;

import com.java.dao.CustomerRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.java.dao.Customer;;

@RestController
@RequestMapping("/1/customer")
@Validated
public class CustomerController {

	@Autowired
	private CustomerRepoService mCustomerService;

	@GetMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") @NotBlank String pId) {
		long id = Long.parseLong(pId);

		return ResponseEntity.ok(mCustomerService.retrieve(id));
	}

	@PostMapping
	@ResponseBody
	public ResponseEntity<Customer> createCustomer(Customer pCustomer) {
		if (pCustomer == null || pCustomer.getId() != null) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.created(null).body(mCustomerService.save(pCustomer));
	}

	@PutMapping
	@ResponseBody
	public ResponseEntity<Customer> updateCustomer(Customer pCustomer) {
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
	public ResponseEntity<Void> deleteCustomer(@PathVariable("id") @NotBlank String pId) {
		long id = Long.parseLong(pId);

		mCustomerService.delete(id);

		return ResponseEntity.accepted().build();
	}

}
