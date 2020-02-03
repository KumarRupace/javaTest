package com.java.service;

import com.java.dao.CustomerRepository;
import com.java.entity.Customer;
import com.java.exception.CustomerAlreadyFoundException;
import com.java.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerRepoService {

	@Autowired
	private CustomerRepository mCustomerRepository;

	// TODO implement all DAO api here.

	/**
	 * Get all customers
	 */
	public List<Customer> getAllCustomers(){
		final List<Customer> customers = new ArrayList<>();
		mCustomerRepository.findAll().forEach(customer -> customers.add(customer));
		return customers;
	}

	/**
	 * Get customer by Id
	 * @param pId
	 */
	public Customer getCustomer(final long pId) {
		return mCustomerRepository.findById(pId);
	}


	/**
	 * Add customer
	 * @param pCustomer
	 */
	public Customer addCustomer(Customer pCustomer){
		Optional<Customer> firstName = mCustomerRepository.findByFirstName(pCustomer.getFirstName());
		if(firstName.isPresent()){
			Customer customerFound = firstName.get();
			if(customerFound.getFirstName().equals(pCustomer.getFirstName()) &&
					customerFound.getLastName().equals(pCustomer.getLastName())){
				throw new CustomerAlreadyFoundException("Customer already found: id-"+customerFound.getId());
			}
		}
		return mCustomerRepository.save(pCustomer);
	}

	/**
	 * Update customer
	 * @param pCustomer, pId
	 */
	public void updateCustomer(long pId, Customer pCustomer) throws CustomerNotFoundException, Exception {
		Customer customer = mCustomerRepository.findById(pId);
		customer.setFirstName(pCustomer.getFirstName());
		customer.setLastName(pCustomer.getLastName());
		 mCustomerRepository.save(customer);
	}

	/**
	 * Delete customer by Id
	 * @param pId
	 */
	public void deleteById(long pId){
		mCustomerRepository.deleteById(pId);
	}


}
