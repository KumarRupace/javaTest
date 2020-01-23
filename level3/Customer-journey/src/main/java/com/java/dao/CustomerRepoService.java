package com.java.dao;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Customer.
 *
 */
@Service
public class CustomerRepoService {

    @Autowired
    private CustomerRepository mCustomerRepository;

    /**
     * Add new or update existing customer.
     *
     * @param customer Customer with details
     * @return updated {@link Customer}
     */
    public Customer addOrUpdateCustomer(final Customer customer) {
        return mCustomerRepository.save(customer);
    }

    /**
     * Delete existing customer.
     *
     * @param customerId customer Id
     */
    public void deleteExistingCustomer(final long customerId) {
        final Customer customer = getCustomerById(customerId);
        if (customer == null) {
            // not an existing customer
            throw new RuntimeException("Customer is invalid");
        }
        mCustomerRepository.deleteById(customerId);
    }

    /**
     * Get customer by Id.
     *
     * @param customerId customer Id
     * @return {@link Customer} or null if customer does not exists
     */
    public Customer getCustomerById(final long customerId) {
        return mCustomerRepository.findById(customerId);
    }

    /**
     * Get all customers.
     * 
     * @return collection of {@link Customer}
     */
    public Collection<Customer> getCustomers() {
        final Collection<Customer> customers = new ArrayList<>();
        mCustomerRepository.findAll().forEach(c -> customers.add(c));
        return customers;
    }
}
