package com.java.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Customer Repository
 * 
 * @author Kimi Qian Min
 *
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	@Query("select c from Customer c where c.lastName=:lastName")
	List<Customer> findByLastName(@Param("lastName") String lastName);

}
