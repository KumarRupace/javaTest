package com.customer.journey.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.customer.journey.dao.CustomerRepository;
import com.customer.journey.domain.Customer;
import com.customer.journey.exception.ApplicationException;
import com.customer.journey.util.CustomerConstants;

/**
 * @author Jhonson
 *
 */
@Service
public class CustomerRepoService implements ICustomerRepoService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	MessageSource messageSource;

	public Customer findById(long id) throws ApplicationException {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new ApplicationException(CustomerConstants.CUST_NOT_FOUND));
		return customer;
	}

	public List<Customer> getAllCustomers() {
		return (List<Customer>) customerRepository.findAll();
	}

	@Override
	@Transactional
	public Customer addCustomer(Customer customer) throws ApplicationException {

		return customerRepository.save(customer);
	}

	@Override
	@Transactional
	public Customer updateCustomer(Customer customer) throws ApplicationException {

		customerRepository.findById(customer.getId())
				.orElseThrow(() -> new ApplicationException(CustomerConstants.UPDATE_FAIL));

		return customerRepository.save(customer);
	}

	@Override
	@Transactional
	public Customer deleteCustomer(Customer customer) throws ApplicationException {

		customerRepository.findById(customer.getId())
				.orElseThrow(() -> new ApplicationException(CustomerConstants.DELETE_FAIL));

		customerRepository.delete(customer);

		return customer;
	}

	@Override
	@Transactional
	public List<Customer> addAllCustomer(List<Customer> pCustomers) {
	
		return StreamSupport.stream( customerRepository.saveAll(pCustomers).spliterator(), false)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<Customer> updateAllCustomer(List<Customer> pCustomers) {
		
		pCustomers.forEach(customer -> {
					customerRepository.findById(customer.getId())
							.orElseThrow(() -> new ApplicationException(CustomerConstants.UPDATE_FAIL));
			  });
		
		return StreamSupport.stream( customerRepository.saveAll(pCustomers).spliterator(), false)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<Customer> deleteAllCustomer(List<Customer> pCustomers) {
		
		pCustomers.forEach(customer -> {
			customerRepository.findById(customer.getId())
					.orElseThrow(() -> new ApplicationException(CustomerConstants.DELETE_FAIL));
	    });

		customerRepository.deleteAll(pCustomers);
		
		return pCustomers;
	}

}
