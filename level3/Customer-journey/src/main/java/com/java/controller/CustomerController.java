package com.java.controller;

import com.java.dao.Customer;
import com.java.dao.CustomerRepoService;
import com.java.exceptions.UnableToSaveException;
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

    private final CustomerRepoService customerRepoService;

    @Autowired
    CustomerController(CustomerRepoService customerRepoService) {
        this.customerRepoService = customerRepoService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Customer>> getAllCustomers() {
        Iterable<Customer> customers = customerRepoService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Long customerId) {
        Customer existingCustomer = customerRepoService.getCustomerById(customerId);
        return new ResponseEntity(existingCustomer, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Customer> saveCustomer(@Valid @RequestBody Customer customerToSave) throws UnableToSaveException {
        Customer savedCustomer = customerRepoService.saveNewCustomer(customerToSave);
        return new ResponseEntity(savedCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") Long customerId,
                                                   @Valid @RequestBody Customer customerToUpdate) throws UnableToSaveException {
        Customer updatedCustomer = customerRepoService.updateExistingCustomer(customerId, customerToUpdate);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") Long customerId) {
        customerRepoService.deleteExistingCustomer(customerId);
        return ResponseEntity.ok("Customer successfully deleted");
    }
}
