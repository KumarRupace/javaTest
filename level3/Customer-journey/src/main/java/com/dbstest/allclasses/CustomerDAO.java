package com.dbstest.allclasses;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * @author Henry
 */
interface CustomerDAO extends JpaRepository<JpaCustomer, String> {
}
