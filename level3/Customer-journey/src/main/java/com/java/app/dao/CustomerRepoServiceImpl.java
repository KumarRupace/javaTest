package com.java.app.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.app.domain.Customer;
import com.java.app.repository.CustomerRepository;

/**
 * @author prasanna kumar
 *
 */
@Service
public class CustomerRepoServiceImpl implements CustomerRepoService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public Customer save(Customer c) {
		return customerRepository.save(c);
	}

	@Override
	public Customer findById(Long parseInt) {
		 Optional<Customer> customer =customerRepository.findById(parseInt);
		 return customer.get();
	}

	@Override
	public Customer update(Customer customer) {
		 Optional<Customer> cust = customerRepository.findById(customer.getId());

		 Customer cus = cust.get();
		 cus.setFirstName(customer.getFirstName());
		 cus.setLastName(customer.getLastName());
		
		 return customerRepository.save(cust.get());
	}

	@Override
	public String delete(Long id) {
		String returnString  = "";
		Optional<Customer> customer =customerRepository.findById(id);
		try {
			
		customerRepository.delete(customer.get());
		returnString  ="Deleted successfully";
		
		}catch(Exception e) {
			returnString ="Failed to delete";
		}
		
		return returnString;
	}
}
