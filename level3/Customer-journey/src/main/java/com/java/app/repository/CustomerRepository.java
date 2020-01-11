package com.java.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.java.app.domain.Customer;

/**
 * @author prasanna kumar
 *
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
