package com.java.dao;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CustomerRepoService provide service to access customer repository
 * 
 * @author Kimi Qian Min
 *
 */
@Service
public class CustomerRepoService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CustomerRepository customerRepository;

	/**
	 * Get customer by id
	 * 
	 */
	public Customer getCustomer(Long id) {
		return this.customerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("The id is invalid"));
	}

	/**
	 * Delete customer by id
	 * 
	 */
	public boolean deleteCustomer(Long id) {
		Customer existing = this.getCustomer(id);
		this.customerRepository.delete(existing);
		return true;
	}

	/**
	 * Create a new customer or update customer if existing
	 * 
	 */
	public Customer addOrUpdateCustomer(Customer customerDto) {

		logger.info(ReflectionToStringBuilder.toString(customerDto, ToStringStyle.MULTI_LINE_STYLE));

		Customer savedCustomer = null;
		if (customerDto.getId() != null) {
			Customer customer = this.customerRepository.findById(customerDto.getId()).get();
			BeanUtils.copyProperties(customerDto, customer);
			savedCustomer = this.customerRepository.save(customer);
		} else {
			Customer customer = new Customer();
			BeanUtils.copyProperties(customerDto, customer);
			savedCustomer = this.customerRepository.save(customer);
		}
		return savedCustomer;
	}

	/**
	 * Get customer by last name
	 * 
	 */
	public List<Customer> getCustomerByLastName(String lastName) {

		logger.info("lastName - {}", lastName);

		return this.customerRepository.findByLastName(lastName);
	}

}
