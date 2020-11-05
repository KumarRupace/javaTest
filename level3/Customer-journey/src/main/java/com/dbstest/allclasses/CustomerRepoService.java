package com.dbstest.allclasses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
/**
 * @author Henry
 */
@Repository
public class CustomerRepoService {
    @Autowired
    CustomerDAO customerDAO;
    @Autowired
    CustomerMapper mapper;

    public Customer saveCustomer(Customer customer) throws AppException {
        if (!customerDAO.existsById(customer.getId()))
            return mapper.jpaToCustomer(customerDAO.saveAndFlush(mapper.customerToJpa(customer)));
        else
            throw new AppException("Customer already exist, cannot overwrite");
    }

    public Customer updateCustomer(Customer customer) throws AppException {
        if (customerDAO.existsById(customer.getId()))
            return mapper.jpaToCustomer(customerDAO.saveAndFlush(mapper.customerToJpa(customer)));
        else
            throw new AppException("Customer does not exist, unable to update");
    }

    @Transactional
    public void deleteCustomer(String id) throws AppException {
        if (customerDAO.existsById(id))
            customerDAO.deleteById(id);
        else
            throw new AppException("Customer not found, unable to delete");
    }

    public Customer getCustomer(String id) throws AppException {
        if (customerDAO.existsById(id))
            return mapper.jpaToCustomer(customerDAO.findById(id).get());
        else
            throw new AppException("Customer not found");
    }
}
