package com.java.dao;

import java.util.List;
import java.util.Optional;

import com.java.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	List<Customer> findByLastName(String lastName);

	Optional<Customer> findByFirstName(String firstName);

	Customer findById(long id);
}
