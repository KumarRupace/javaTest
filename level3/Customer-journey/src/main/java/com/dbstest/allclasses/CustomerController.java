package com.dbstest.allclasses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * @author Henry
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerRepoService customerRepoService;

    @PostMapping
    Customer createCustomer(@RequestBody Customer customer) throws AppException {
        return customerRepoService.saveCustomer(customer);
    }

    @PutMapping
    Customer updateCustomer(@RequestBody Customer customer) throws AppException {
        return customerRepoService.updateCustomer(customer);
    }

    @DeleteMapping("/{id}")
    void deleteCustomer(@PathVariable String id) throws AppException {
        customerRepoService.deleteCustomer(id);
    }

    @GetMapping("/get/{id}")
    Customer getCustomer(@PathVariable String id) throws AppException {
        return customerRepoService.getCustomer(id);
    }
}
