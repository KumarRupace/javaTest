package com.customer.journey.service;

import java.util.List;
import com.customer.journey.domain.Customer;
import com.customer.journey.exception.ApplicationException;

public interface ICustomerRepoService {
	
	public Customer findById(long id) throws ApplicationException ;	
	public List<Customer> getAllCustomers () ;	
	public Customer addCustomer (Customer customer) throws ApplicationException ;	
	public Customer updateCustomer (Customer customer) throws ApplicationException;	
	public Customer deleteCustomer (Customer customer) throws ApplicationException ;
	public List<Customer> addAllCustomer(List<Customer> pCustomers);	
	public List<Customer> updateAllCustomer(List<Customer> pCustomers);
	public List<Customer> deleteAllCustomer(List<Customer> pCustomers);
	
}
