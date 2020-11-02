package com.java.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerRepoService {

	@Autowired
	private CustomerRepository mCustomerRepository;

	// TODO implement all DAO api here.
	public Customer retrieve(long id) {
		return mCustomerRepository.findById(id);
	}

	public Customer save(Customer customer) {
		return mCustomerRepository.save(customer);
	}

	public boolean exist(long id) {
		return mCustomerRepository.existsById(id);
	}

	public void delete(long id) {
		mCustomerRepository.deleteById(id);
	}

}
