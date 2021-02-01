package com.java.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerRepoService {

	@Autowired
	private CustomerRepository mCustomerRepository;

	public Customer updateOrAddCustomer(Customer customer) {
		return mCustomerRepository.save(customer);
	}

	public void deleteCustomer(String id) {
		mCustomerRepository.deleteById(Long.parseLong(id));
	}

	public Customer findCustomerById(String id) {
		return mCustomerRepository.findById(Long.parseLong(id));
	}
}
