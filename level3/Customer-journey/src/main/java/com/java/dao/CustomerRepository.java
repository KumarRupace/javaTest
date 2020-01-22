package com.java.dao;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

//Author: Zhang Xin

public interface CustomerRepository extends CrudRepository<Customer, Long> {
	 
	 List<Customer> findByLastName(String lastName);
	 List<Customer> findByFirstName(String firstName);
	 Customer findById(long id);
	 Customer findByNric(String nric);
	 Customer findByNumber(String nric);
}
