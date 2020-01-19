package com.java.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerRepoServiceTest {

	@Autowired
	private CustomerRepoService customerRepoService;

	@Test
	public void test1BeanLoadSuccess() {
		assertThat(customerRepoService).isNotNull();
	}

	@Test
	public void test2CreateCustomerSuccess() {
		Customer dto = new Customer("kimi 123124", "qian 123213");
		Customer createdCustomer = this.customerRepoService.addOrUpdateCustomer(dto);

		assertThat(createdCustomer).isNotNull();
		assertThat(createdCustomer.getId()).isNotNull();
		assertEquals(createdCustomer.getFirstName(), dto.getFirstName());
		assertEquals(createdCustomer.getLastName(), dto.getLastName());
	}

	@Test
	public void test3GetCustomerByLastNameSuccess() {
		String randomString = RandomStringUtils.random(5);
		Customer dto = new Customer("Kimi_" + randomString, "Qian_" + randomString);
		Customer createdCustomer = this.customerRepoService.addOrUpdateCustomer(dto);

		Customer getCustomer = this.customerRepoService.getCustomerByLastName(createdCustomer.getLastName()).get(0);

		assertThat(getCustomer).isNotNull();
		assertEquals(getCustomer.getFirstName(), dto.getFirstName());
		assertEquals(getCustomer.getLastName(), dto.getLastName());
	}

	@Test
	public void test4deleteCustomerSuccess() {
		String randomString = RandomStringUtils.random(5);
		Customer dto = new Customer("Kimi_" + randomString, "Qian_" + randomString);
		Customer createdCustomer = this.customerRepoService.addOrUpdateCustomer(dto);

		boolean deleteFlag = this.customerRepoService.deleteCustomer(createdCustomer.getId());

		assertThat(deleteFlag).isNotNull();
		assertThat(deleteFlag).isTrue();
	}

	@Test
	public void test5CreateCustomerSuccess_withCustomerId() {
		List<Customer> list = customerRepoService.getCustomerByLastName("qian 123213");
		if (!list.isEmpty()) {
			Customer customer = list.get(0);

			Customer dto = new Customer();
			BeanUtils.copyProperties(customer, dto);

			Customer updatedCustomer = customerRepoService.addOrUpdateCustomer(dto);
			assertThat(updatedCustomer).isNotNull();
			assertThat(updatedCustomer.getId()).isNotNull();
			assertEquals(customer.getId(), updatedCustomer.getId());
			assertEquals(customer.getFirstName(), updatedCustomer.getFirstName());
			assertEquals(customer.getLastName(), updatedCustomer.getLastName());
		}

	}

}
