package com.java.controller;

import com.java.dao.Customer;
import com.java.dao.CustomerRepoService;
import com.java.exception.UnableToSaveException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = {"/api/v1/customers"}, produces = APPLICATION_JSON_VALUE)
public class CustomerController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	private static final String FETCH_CUSTOMERS_LOG = "Customer records fetched";
	private static final String NEW_CUSTOMER_LOG = "New customer record created as :{}";
	private static final String FETCH_CUSTOMER_LOG = "Customer record fetched as : {}";
	private static final String UPDATE_CUSTOMER_LOG = "Customer record updated as :{}";
	private static final String DELETE_CUSTOMER_LOG = "Customer record deleted as :{}";

	private final CustomerRepoService mCustomerRepoService;

	@Autowired
	public CustomerController(CustomerRepoService customerRepoService) {
		this.mCustomerRepoService = customerRepoService;
	}

	@Operation(summary = "Fetch all existing customer records")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the customer record", content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
			@ApiResponse(responseCode = "400", description = "Customer record not found", content = @Content)})
	@GetMapping
	public ResponseEntity<Iterable<Customer>> getAllCustomers() {
		Iterable<Customer> customers = mCustomerRepoService.getAllCustomers();
		logger.info(FETCH_CUSTOMERS_LOG);
		//Publish Event : Customers Retrieved with Fetched Message JSON
		return ResponseEntity.ok(customers);
	}

	@Operation(summary = "Get an existing customer record based on Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get the customer record", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Customer.class))}),
			@ApiResponse(responseCode = "400", description = "No such customer found", content = @Content)})
	@GetMapping("/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Long customerId) {
		Customer existingCustomer = mCustomerRepoService.retrieveCustomerById(customerId);
		logger.info(FETCH_CUSTOMER_LOG, existingCustomer.toString());
		//Publish Event : Customer Retrieved with Fetched Message JSON
		return new ResponseEntity(existingCustomer, HttpStatus.OK);
	}

	@Operation(summary = "Creates a new customer record")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "New customer record created", content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
			@ApiResponse(responseCode = "400", description = "Bad input", content = @Content),
	})
	@PostMapping
	public ResponseEntity<Customer> saveCustomer(@Valid @RequestBody Customer customerToSave) throws UnableToSaveException {
		Customer savedCustomer = mCustomerRepoService.createCustomer(customerToSave);
		logger.info(NEW_CUSTOMER_LOG, savedCustomer.toString());
		//Publish Event : New customer record created event with JSON data to be published
		return new ResponseEntity(savedCustomer, HttpStatus.CREATED);
	}

	@Operation(summary = "Update an existing customer record based on Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Customer record updated", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Customer.class))}),
			@ApiResponse(responseCode = "400", description = "Bad input", content = @Content),
	})
	@PutMapping("/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable("id") Long customerId,
												   @Valid @RequestBody Customer customerToUpdate) throws UnableToSaveException {
		Customer updatedCustomer = mCustomerRepoService.updateCustomer(customerId, customerToUpdate);
		logger.info(UPDATE_CUSTOMER_LOG, updatedCustomer.toString());
		//Publish Event : Customer record updated event with JSON data to be published
		return ResponseEntity.ok(updatedCustomer);
	}

	@Operation(summary = "Delete a Customer record based on Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Customer record deleted", content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
			@ApiResponse(responseCode = "400", description = "Bad input", content = @Content),
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable("id") Long customerId) {
		mCustomerRepoService.deleteCustomer(customerId);
		logger.info(DELETE_CUSTOMER_LOG, customerId);
		//Publish Event : Customer deleted Message JSON
		return ResponseEntity.ok("Customer successfully deleted");
	}
}
