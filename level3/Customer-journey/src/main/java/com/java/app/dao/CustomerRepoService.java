package com.java.app.dao;

import com.java.app.domain.Customer;

/**
 * @author prasanna kumar
 *
 */
public interface CustomerRepoService {

	/**
	 * @param parseInt
	 * @return Customer
	 */
	Customer findById(Long parseInt);
	
	/**
	 * @param customer
	 * @return Customer
	 */
	Customer save(Customer customer);
	
	
	/**
	 * @param customer
	 * @returnCustomer
	 */
	Customer update(Customer customer);
	
	  /**
	 * @param id
	 */
	String delete(Long id);
}
