package com.java.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerRepoService {

	@Autowired
	private CustomerRepository customerRepository;

	// TODO implement all DAO api here.
	public Customer getCustomerById(long id) {
		return customerRepository.findById(id);
	}

	public Customer addOrUpdateCustomer(Customer customer) {
		return customerRepository.save(customer);
	}

	public void deleteCustomer(long id) {
		customerRepository.deleteById(id);
	}

}
