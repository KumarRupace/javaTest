package com.java.dao;

import com.java.exceptions.NotFoundException;
import com.java.exceptions.UnableToDeleteException;
import com.java.exceptions.UnableToSaveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerRepoService {

    private final CustomerRepository customerRepository;

    @Autowired
    CustomerRepoService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Iterable<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long customerId) throws NotFoundException {
        return customerRepository
                .findById(customerId)
                .orElseThrow(() -> new NotFoundException(customerId));
    }

    public Customer saveNewCustomer(Customer customerToSave) throws UnableToSaveException {
        if(customerToSave.getId() != null)
            throw new UnableToSaveException("ID must be null. Please provide Customer details without an ID.");
        try {
            return customerRepository.save(customerToSave);
        } catch (Exception exception) {
            throw new UnableToSaveException(exception.getMessage());
        }
    }

    public Customer updateExistingCustomer(Long customerId,
                                           Customer customerToUpdate) throws NotFoundException, UnableToSaveException {
        Customer existingCustomer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new NotFoundException(customerId));
        try {
            existingCustomer.setFirstName(customerToUpdate.getFirstName());
            existingCustomer.setLastName(customerToUpdate.getLastName());
            return customerRepository.save(existingCustomer);
        } catch (Exception exception) {
            throw new UnableToSaveException(exception.getMessage());
        }
    }

    public void deleteExistingCustomer(Long customerId) throws NotFoundException, UnableToDeleteException {
        Customer existingCustomer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new NotFoundException(customerId));
        try {
            customerRepository.delete(existingCustomer);
        } catch (Exception exception) {
            throw new UnableToDeleteException(exception.getMessage());
        }
    }
}
