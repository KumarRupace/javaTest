package com.customer.journey.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.customer.journey.domain.Customer;
import com.customer.journey.exception.ApplicationException;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class CustomerRepoServiceTest {
	
	 @Autowired
	 private ICustomerRepoService customerRepoService;

	 @Test
	 @Order(1) 
	 public void sanity_check_with_list_All_customers() throws Exception {	
		 
		 List<Customer> customers = customerRepoService.getAllCustomers();			
		 assertThat (customers != null);
		 assertThat(customers.size() > 0);
					
		}
	 
	 @Test
	 @Order(2) 
	 public void sanity_check_with_find_by_customer_id_1() throws Exception {	
		 Customer createdCustomer = customerRepoService.findById(1);
		 assertThat(createdCustomer.getId().equals(1));
		 assertThat(createdCustomer.getFirstName().equals("Shiva"));
		 assertThat(createdCustomer.getFirstName().equals("MURUGAN"));
					
		}
	 
	  @Test
	  @Order(3) 
	  public void sanity_check_with_create_customer_1() throws Exception {
		 Customer createdCustomer = customerRepoService
				 					.addCustomer(new Customer("Antony","Chow"));		 
		 assertThat(createdCustomer.getFirstName().equals("Antony"));
		 assertThat(createdCustomer.getLastName().equals("Chow"));
		 
	 }
	  
	  @Test
	  @Order(4) 
	  public void sanity_check_with_delete_customer_1() throws Exception {
		 Customer deletedCustomer = customerRepoService
				 					.deleteCustomer(new Customer(Long.valueOf(6),"Guoyao","WEIMING"));		 
		 assertThat(deletedCustomer.getFirstName().equals("Guoyao"));
		 assertThat(deletedCustomer.getLastName().equals("WEIMING"));
		 
	 }
	  
	  @Test
	  @Order(5) 
	  public void sanity_check_with_update_customer_1() throws Exception {
		 Customer updatedCustomer = customerRepoService
				 					.updateCustomer(new Customer(Long.valueOf(3),"David","JOHN"));		 
		 assertThat(updatedCustomer.getFirstName().equals("David"));
		 assertThat(updatedCustomer.getLastName().equals("JOHN"));
		 
	 }
	  
	  @Test
	  @Order(6) 
	  public void thrw_customer_not_found_by_id_1() throws Exception {
		  
		  ApplicationException exceptionThrown = assertThrows ( 
				  				ApplicationException.class, () -> customerRepoService.findById(21));

	      assertThat(exceptionThrown.getMessage()).isEqualTo("customer.not.found");
	    }
	  
	  @Test
	  @Order(7) 
	  public void thrw_customer_update_failure_1() throws Exception {
		  
		  ApplicationException exceptionThrown = assertThrows ( 
				  				ApplicationException.class, () 
				  				   -> customerRepoService.updateCustomer(new Customer(Long.valueOf(20),"Tony","Bob")));

	      assertThat(exceptionThrown.getMessage()).isEqualTo("customer.update.fail");
	    }
	  
	  @Test
	  @Order(8) 
	  public void thrw_customer_delete_failure_1() throws Exception {
		  
		  ApplicationException exceptionThrown = assertThrows ( 
				  				ApplicationException.class, () 
				  				   -> customerRepoService.deleteCustomer(new Customer(Long.valueOf(22),"Tony","Bob")));

	      assertThat(exceptionThrown.getMessage()).isEqualTo("customer.delete.fail");
	    }
	  
	  
}
