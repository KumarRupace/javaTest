package com.assesmentdbs.customerjourney.controller;

import com.assesmentdbs.customerjourney.exceptions.CustomerAlreadyPresentException;
import com.assesmentdbs.customerjourney.exceptions.CustomerNotFoundException;
import com.assesmentdbs.customerjourney.model.Customer;
import com.assesmentdbs.customerjourney.service.CustomerRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Prithvi Panchapakeshan
 * RestController for mapping all the REST Apis.
 * Please use swagger (http://localhost:8080/swagger-ui.html) to test these links
 */
@RestController
@RequestMapping("/customer-service")
public class CustomerController {

    CustomerRepoService customerRepoService;

    @Autowired
    public CustomerController(CustomerRepoService customerRepoService) {
        this.customerRepoService = customerRepoService;
    }

    /**
     * Get all the customers.
     * @return
     */
    @GetMapping("/customers")
    public List<Customer> getAllCustomers(){
        return customerRepoService.getAllCustomers();
    }

    /**
     * Get customer using customer Id.
     * @param id
     * @return
     */
    @GetMapping("/customers/{id}")
    public Customer getCustomerById(@PathVariable Long id){
        return customerRepoService.getCustomerById(id);
    }

    /**
     * Add a new customer.
     * @param customer
     * @return
     */
    @PostMapping("/customers")
    public Customer addCustomer(@RequestBody Customer customer){
        return customerRepoService.addCustomer(customer);
    }

    /**
     * Modify existing customer details.
     * @param id - Existing customer id
     * @param customer
     * @return
     */
    @PutMapping("/customers/{id}")
    public Customer updateCustomer(@PathVariable Long id,@RequestBody Customer customer){
        return customerRepoService.updateCustomer(id,customer);
    }

    /**
     * Delete customer
     * @param id
     * @return
     */
    @DeleteMapping("/customers/{id}")
    public Customer deleteCustomer(@PathVariable Long id){
        return customerRepoService.delete(id);
    }
}
