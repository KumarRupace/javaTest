package com.java.dao;

import com.java.exception.IllegalCustomerArgumentException;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("customerRepoService")
public class CustomerRepoService {

	@Autowired
	private CustomerRepository customerRepository;

	public Customer getCustomerById(long id) {
		return customerRepository.findById(id);
	}

	public Customer createCustomerByFirstLastName(String firstName, String lastName) {
		return customerRepository.save(new Customer(firstName, lastName));
	}

	public Customer updateOrCreateCustomer(Customer customer) {
		if (customer == null) {
			throw new IllegalCustomerArgumentException("Customer is null");
		}
		return customerRepository.save(customer);
	}

	public void deleteCustomerById(long id) {
		customerRepository.deleteById(id);
	}

	public List<Customer> findCustomersByLastName(String lastName) {
		if (StringUtils.isBlank(lastName)) {
			throw new IllegalCustomerArgumentException("lastName is blank");
		}
		return customerRepository.findByLastName(lastName);
	}
}
