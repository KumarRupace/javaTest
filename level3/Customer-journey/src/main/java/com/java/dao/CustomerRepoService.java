package com.java.dao;

import com.java.exception.EntityNotFoundException;
import com.java.exception.UnableToDeleteException;
import com.java.exception.UnableToGetException;
import com.java.exception.UnableToSaveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class CustomerRepoService {

	private final CustomerRepository mCustomerRepository;

	@Autowired
	public CustomerRepoService(CustomerRepository mCustomerRepository) {
		this.mCustomerRepository = mCustomerRepository;
	}

	public Iterable<Customer> getAllCustomers() {
		var result = mCustomerRepository.findAll();
		if(result == null) {
			throw new UnableToGetException("Unable to get the object");
		}
		return result;
	}

	@Transactional
	public Customer createCustomer(Customer customerToSave) throws UnableToSaveException {

		if(customerToSave.getId() != null)
			throw new UnableToSaveException("Id must be null. Please provide Customer details without an Id.");

		if(customerToSave == null) {
			throw new UnableToSaveException("Data must not be null. Please provide valid customer details.");
		}
		try {
			return mCustomerRepository.save(customerToSave);
		} catch (Exception exception) {
			throw new UnableToSaveException(exception.getMessage());
		}
	}

	public Customer retrieveCustomerById(Long customerId) throws EntityNotFoundException {
		if(customerId == null) {
			throw new UnableToGetException("Customer Id must not be null. Please provide a valid customer Id.");
		}
		return mCustomerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException(String.format("No customer found with id: %d", customerId)));
	}

	public List<Customer> findByLastName(String lastName) throws EntityNotFoundException {
		if(lastName == null) {
			throw new UnableToGetException("Last Name must not be null. Please provide a valid customer last name.");
		}
		return mCustomerRepository.findByLastName(lastName);
	}

	@Transactional
	public Customer updateCustomer(Long customerId, Customer customerToUpdate) throws EntityNotFoundException, UnableToSaveException {

		if(customerId == null)
			throw new UnableToSaveException("Id must not be null. Please provide valid customer Id.");

		if(customerToUpdate == null) {
			throw new UnableToSaveException("Data must not be null. Please provide valid customer details.");
		}

		Customer existingCustomer = mCustomerRepository
				.findById(customerId)
				.orElseThrow(() -> new EntityNotFoundException(String.format("No customer found with id: %d Please try with a valid customer Id.", customerId)));
		try {
			existingCustomer.setFirstName(customerToUpdate.getFirstName());
			existingCustomer.setLastName(customerToUpdate.getLastName());
			return mCustomerRepository.save(existingCustomer);
		} catch (Exception exception) {
			throw new UnableToSaveException(exception.getMessage());
		}
	}

	@Transactional
	public Customer deleteCustomer(Long customerId) throws EntityNotFoundException, UnableToDeleteException {

		if(customerId == null) {
			throw new UnableToDeleteException("Id must not be null. Please provide a valid customer Id.");
		}

		Customer existingCustomer = mCustomerRepository
				.findById(customerId)
				.orElseThrow(() -> new EntityNotFoundException(String.format("No customer found with id: %d Please try with a valid customer Id.", customerId)));
		try {
			mCustomerRepository.delete(existingCustomer);
			return existingCustomer;
		} catch (Exception exception) {
			throw new UnableToDeleteException(exception.getMessage());
		}
	}
}
