package com.java.controller;

import com.java.dao.Customer;
import com.java.dao.CustomerRepoService;
import com.java.exceptions.UnableToSaveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/customers")
@Validated
public class CustomerController {

    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerRepoService customerRepoService;

    @Autowired
    CustomerController(CustomerRepoService customerRepoService) {
        this.customerRepoService = customerRepoService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Customer>> getAllCustomers() {
        logger.info("Request received to fetch all the existing customers.");
        Iterable<Customer> customers = customerRepoService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Long customerId) {
        logger.info(String.format("Request received to fetch the existing customer with id: %s.", customerId));
        Customer existingCustomer = customerRepoService.getCustomerById(customerId);
        return new ResponseEntity<>(existingCustomer, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Customer> saveCustomer(@Valid @RequestBody Customer customerToSave) throws UnableToSaveException {
        logger.info("Request received to save a new customer.");
        Customer savedCustomer = customerRepoService.saveNewCustomer(customerToSave);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") Long customerId,
                                                   @Valid @RequestBody Customer customerToUpdate) throws UnableToSaveException {
        logger.info(String.format("Request received to update an existing customer with id: %s", customerId));
        Customer updatedCustomer = customerRepoService.updateExistingCustomer(customerId, customerToUpdate);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") Long customerId) {
        logger.info(String.format("Request received to delete an existing customer with id: %s", customerId));
        customerRepoService.deleteExistingCustomer(customerId);
        return ResponseEntity.ok("Customer successfully deleted");
    }
}
