package com.java.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository("customerRepository")
public interface CustomerRepository extends CrudRepository<Customer, Long> {
	@Query("SELECT c FROM Customer c WHERE c.lastName = :lastName")
	List<Customer> findByLastName(@Param("lastName") String lastName);

	Customer findById(long id);
}
