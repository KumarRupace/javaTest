package com.dbstest.allclasses;

import org.springframework.stereotype.Component;
/**
 * @author Henry
 */
@Component
public class CustomerMapper {

    Customer jpaToCustomer(JpaCustomer jpaCustomer) {
        Customer customer = new Customer();
        customer.setId(jpaCustomer.getId());
        customer.setDateOfBirth(jpaCustomer.getDateOfBirth());
        customer.setGender(Gender.valueOf(jpaCustomer.getGender()));
        customer.setName(jpaCustomer.getName());
        return customer;
    }

    JpaCustomer customerToJpa(Customer customer) {
        JpaCustomer jpaCustomer = new JpaCustomer();
        jpaCustomer.setId(customer.getId());
        jpaCustomer.setDateOfBirth(customer.getDateOfBirth());
        jpaCustomer.setGender(customer.getGender().name());
        jpaCustomer.setName(customer.getName());
        return jpaCustomer;
    }
}
