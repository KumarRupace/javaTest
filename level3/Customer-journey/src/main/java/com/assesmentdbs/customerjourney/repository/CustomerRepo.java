package com.assesmentdbs.customerjourney.repository;

import com.assesmentdbs.customerjourney.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Prithvi Panchapakeshan
 */
@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    public Optional<Customer> findByFirstName(String firstName);

    public Optional<Customer> findByLastName(String lastName);

}
