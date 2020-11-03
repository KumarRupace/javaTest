package com.java.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerRepoService {

	@Autowired
	private CustomerRepository mCustomerRepository;

	// TODO implement all DAO api here.
	@Transactional(readOnly = true)
	public Customer retrieve(long id) {
		return mCustomerRepository.findById(id);
	}

	public Customer save(Customer customer) {
		return mCustomerRepository.save(customer);
	}

	@Transactional(readOnly = true)
	public boolean exists(long id) {
		return mCustomerRepository.existsById(id);
	}

	public void delete(long id) {
		mCustomerRepository.deleteById(id);
	}

}
