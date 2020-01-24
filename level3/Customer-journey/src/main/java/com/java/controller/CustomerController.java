package com.java.controller;

import java.util.Collection;

import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.java.dao.Customer;
import com.java.dao.CustomerRepoService;;

@RestController
@RequestMapping("/1/customer")
@Validated
public class CustomerController {

    private CustomerRepoService customerRepoService;

    /**
     * Constructor. Inject service into controller.
     *
     * @param customerRepoService CustomerRepoService
     */
    @Autowired
    public CustomerController(final CustomerRepoService customerRepoService) {
        this.customerRepoService = customerRepoService;
    }

    /**
     * Get all customers.
     *
     * @return collection of customers
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<Collection<Customer>> getCustomers() {
        final Collection<Customer> customers = customerRepoService.getCustomers();
        if (CollectionUtils.isEmpty(customers)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customers);
    }

    /**
     * Get customer by Id.
     *
     * @param pId customer id
     * @return customer
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") @NotBlank final String pId) {
        if (!NumberUtils.isNumber(pId)) {
            // not a number
            return ResponseEntity.badRequest().build();
        }
        Customer customer = customerRepoService.getCustomerById(Long.valueOf(pId));
        if (customer == null) {
            // return bad request instead of no content - security reason so that consumer
            // will not be able to guess if there is such a customer or not
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(customer);
    }

    /**
     * Delete customer.
     *
     * @param pId customer Id
     * @return String
     */
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") @NotBlank final String pId) {
        if (!NumberUtils.isNumber(pId)) {
            // not a number
            return ResponseEntity.badRequest().build();
        }
        try {
            customerRepoService.deleteExistingCustomer(Long.valueOf(pId));
        } catch (Exception e) {
            // if customer does not exists, exception will be thrown
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok("Success");
    }

    /**
     * Add or update customer.
     *
     * @param pCustomer Customer
     * @return updated Customer
     */
    @PutMapping(value = "/", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseEntity<Customer> addOrUpdateCustomer(@RequestBody final Customer pCustomer) {
        final Customer customer = customerRepoService.addOrUpdateCustomer(pCustomer);
        return ResponseEntity.ok(customer);
    }

}
