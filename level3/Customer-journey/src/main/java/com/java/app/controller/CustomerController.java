package com.java.controller;

import javax.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.java.dao.Customer;;

@RestController
@RequestMapping("/1/customer")
@Validated
public class CustomerController {

	@GetMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") @NotBlank String pId) {
		return null;
		// complete this method
	}

	@PutMapping(value = "/")
	@ResponseBody
	public ResponseEntity<Customer> addOrUpdateCustomer(Customer pCustomer) {
		return null;
		// complete this method
	}

}
