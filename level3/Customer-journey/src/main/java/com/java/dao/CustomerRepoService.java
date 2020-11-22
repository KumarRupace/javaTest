package com.java.dao;

import com.java.exceptions.NotFoundException;
import com.java.exceptions.UnableToDeleteException;
import com.java.exceptions.UnableToGetException;
import com.java.exceptions.UnableToSaveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CustomerRepoService {

    private final Logger logger = LoggerFactory.getLogger(CustomerRepoService.class);

    private final CustomerRepository customerRepository;

    @Autowired
    CustomerRepoService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Iterable<Customer> getAllCustomers() throws UnableToGetException {
        logger.info("Returning all existing customers in the repository.");
        try {
            return customerRepository.findAll();
        } catch (Exception exception) {
            logger.error("Error retrieving all the customers.");
            throw new UnableToGetException(exception.getMessage());
        }
    }

    public Customer getCustomerById(Long customerId) throws UnableToGetException, NotFoundException {
        if (customerId == null) {
            logger.error("Customer ID cannot be null, customer object will not be retrieved from the repository.");
            throw new UnableToGetException("Customer ID must not be null. Please provide a valid customer ID.");
        }
        logger.info(String.format("Returning customer with id: %s from the repository.", customerId));
        return customerRepository
                .findById(customerId)
                .orElseThrow(() -> new NotFoundException(customerId));
    }

    public Customer saveNewCustomer(Customer customerToSave) throws UnableToSaveException {
        logger.info("Saving a new customer in the repository.");
        if (customerToSave == null) {
            logger.error("Customer provided is null, customer object will not be saved in the repository.");
            throw new UnableToSaveException("Customer must not be null. Please provide a valid Customer object.");
        } else if (customerToSave.getId() != null) {
            logger.error("Customer ID is not null, customer object will not be saved in the repository.");
            throw new UnableToSaveException("ID must be null. Please provide Customer details without an ID.");
        }
        try {
            return customerRepository.save(customerToSave);
        } catch (Exception exception) {
            logger.error("Error while attempting to save the new customer in the repository.");
            throw new UnableToSaveException(exception.getMessage());
        }
    }

    public Customer updateExistingCustomer(Long customerId,
                                           Customer customerToUpdate) throws NotFoundException, UnableToSaveException {
        if (customerId == null || customerToUpdate == null) {
            logger.error("Customer ID and updated customer details cannot be null, customer object will not be updated in the repository.");
            throw new UnableToSaveException("Customer ID and customer details must not be null. Please provide a valid customer ID and Customer details.");
        }

        logger.info(String.format("Updating the existing customer with id %s in the repository.", customerId));
        Customer existingCustomer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new NotFoundException(customerId));
        try {
            existingCustomer.setFirstName(customerToUpdate.getFirstName());
            existingCustomer.setLastName(customerToUpdate.getLastName());
            return customerRepository.save(existingCustomer);
        } catch (Exception exception) {
            logger.error(String.format("Error while attempting to update the existing customer in the repository with id: %s.", customerId));
            throw new UnableToSaveException(exception.getMessage());
        }
    }

    public void deleteExistingCustomer(Long customerId) throws NotFoundException, UnableToDeleteException {
        if (customerId == null) {
            logger.error("Customer ID cannot be null, customer object will not be deleted from the repository.");
            throw new UnableToDeleteException("Customer ID must not be null. Please provide a valid customer ID.");
        }

        logger.info(String.format("Deleting the existing customer with id %s from the repository.", customerId));
        Customer existingCustomer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new NotFoundException(customerId));
        try {
            customerRepository.delete(existingCustomer);
        } catch (Exception exception) {
            logger.error(String.format("Error while attempting to delete the existing customer in the repository with id: %s.", customerId));
            throw new UnableToDeleteException(exception.getMessage());
        }
    }
}
