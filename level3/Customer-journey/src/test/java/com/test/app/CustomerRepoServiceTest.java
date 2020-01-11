package com.test.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.java.app.AccessingDataJpaApplication;
import com.java.app.dao.CustomerRepoServiceImpl;
import com.java.app.domain.Customer;
import com.java.app.repository.CustomerRepository;

/**
 * @author prasanna kumar
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccessingDataJpaApplication.class)
public class CustomerRepoServiceTest {
	
	@InjectMocks 
	CustomerRepoServiceImpl CustomerRepoService;
	
	@Mock
	CustomerRepository  CustomerRepository;
	
	/**
	 * Testing the findById()
	 */ 	
	@Test
	public void findByIdTest() {
		Customer cust = new Customer((long)1,"kumar","bala");
		when(CustomerRepository.findById((long) 1)).thenReturn(Optional.of(cust));
		Customer customer = CustomerRepoService.findById((long)1);
		
		assertEquals("kumar",customer.getFirstName());
		assertEquals("bala",customer.getLastName());
		
	}
	
	/**
	 * Testing the updateCustomerTest()
	 */ 	
	@Test
	public void updateCustomerTest() {
		Customer cust = new Customer((long)1,"pavan","bala");
		when(CustomerRepository.save(cust)).thenReturn(cust);
		Customer customer = CustomerRepoService.save(cust);
		
		assertEquals("pavan",customer.getFirstName());
		assertEquals("kumar",customer.getLastName());
		
	}
	
	/**
	 * Testing the updateCustomerTest()
	 */ 	
	@Test
	public void deleteCustomerTest() {
		Customer cust = new Customer((long)1,"pavan","bala");
		when(CustomerRepository.findById((long) 1)).thenReturn(Optional.of(cust));
		CustomerRepoService.delete((long)1);
		
	    Mockito.verify(CustomerRepository, times(1)).delete(cust);

		
		
	}



}
