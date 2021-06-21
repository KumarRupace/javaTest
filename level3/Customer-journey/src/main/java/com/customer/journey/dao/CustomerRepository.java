package com.customer.journey.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.customer.journey.domain.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
	
}
