package com.assesmentdbs.customerjourney.service;

import com.assesmentdbs.customerjourney.exceptions.CustomerAlreadyPresentException;
import com.assesmentdbs.customerjourney.exceptions.CustomerNotFoundException;
import com.assesmentdbs.customerjourney.model.Customer;
import com.assesmentdbs.customerjourney.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Prithvi Panchapakeshan
 * Customer Service Class to implement business logic and
 * call repository operations.
 */
@Service
public class CustomerRepoService {

    CustomerRepo customerRepo;

    @Autowired
    public CustomerRepoService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    /**
     * Fetch all the customers from repository.
     * @return
     */
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    /**
     * Query customer repo to find by Id else throw exception.
     * @param id
     * @return
     */
    public Customer getCustomerById(Long id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("id-"+id));
    }

    /**
     * Add customer to repository if customer is not found.
     * Currently first and last names are used to check if customer exists.
     * This can be extended to do other kinds of check also.
     * @param customer
     * @return
     */
    public Customer addCustomer(Customer customer){
        Optional<Customer> byFirstName = customerRepo.findByFirstName(customer.getFirstName());
        if(byFirstName.isPresent()){
            Customer existingCustomer = byFirstName.get();
            if(existingCustomer.getFirstName().equals(customer.getFirstName()) &&
                    existingCustomer.getLastName().equals(customer.getLastName())){
                throw new CustomerAlreadyPresentException("Customer already exists: id-"+existingCustomer.getId());
            }
        }
        return customerRepo.save(customer);
    }

    /**
     * Update customer if found in repository else throw exception.
     * @param id
     * @param customer
     * @return
     */
    public Customer updateCustomer(Long id,Customer customer){
        Optional<Customer> byId = customerRepo.findById(id);
        if(!byId.isPresent())
            throw new CustomerNotFoundException("Cannot update id-"+id);
        customer.setId(id);
        return customerRepo.save(customer);
    }

    /**
     * perform delete operation, remove from repository.
     * @param id
     * @return
     */
    public Customer delete(Long id){
        Optional<Customer> byId = customerRepo.findById(id);
        if(!byId.isPresent())
            throw new CustomerNotFoundException("Cannot delete id-"+id);
        customerRepo.delete(byId.get());
        return byId.get();
    }
}
