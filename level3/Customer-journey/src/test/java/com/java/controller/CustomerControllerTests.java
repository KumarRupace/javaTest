package com.java.controller;

import com.java.app.AccessingDataJpaApplication;
import com.java.dao.Customer;
import com.java.dao.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static com.java.helper.TestHelper.createACustomer;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccessingDataJpaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerTests {

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    @LocalServerPort
    private int port;

    @Autowired
    private CustomerRepository customerRepository;

    @Before
    public void setup() {
        customerRepository.deleteAll();
    }

    @Test
    public void should_be_able_to_retrieve_customers() {
        //Arrange
        Customer aCustomerDto = createACustomer();
        HttpEntity<Customer> entity = new HttpEntity<>(aCustomerDto, headers);
        restTemplate.exchange(createURLWithPort("/api/v1/customers"), HttpMethod.POST, entity, String.class);
        aCustomerDto = createACustomer();
        entity = new HttpEntity<>(aCustomerDto, headers);
        restTemplate.exchange(createURLWithPort("/api/v1/customers"), HttpMethod.POST, entity, String.class);
        HttpEntity<Customer> customerDtos = new HttpEntity<>(null, headers);
        //Act
        ResponseEntity<Customer[]> response = restTemplate.exchange(createURLWithPort("/api/v1/customers/"), HttpMethod.GET, customerDtos, Customer[].class);
        //Assert
        assertThat(response.getBody().length == 2);
    }

    @Test
    public void should_be_able_to_create_a_customer() {
        //Arrange
        Customer aCustomerDto = createACustomer();
        HttpEntity<Customer> entity = new HttpEntity<>(aCustomerDto, headers);

        //Act
        ResponseEntity<Customer> response = restTemplate.exchange(createURLWithPort("/api/v1/customers"), HttpMethod.POST, entity, Customer.class);

        //Assert
        assertThat(response.getStatusCodeValue() == 201);
        assertThat(response.getBody().equals(aCustomerDto));

    }

    @Test
    public void should_be_able_to_retrieve_a_customer() {
        //Arrange
        Customer aCustomerDto = createACustomer();
        HttpEntity<Customer> entity = new HttpEntity<>(aCustomerDto, headers);
        ResponseEntity<Customer> response1 = restTemplate.exchange(createURLWithPort("/api/v1/customers"), HttpMethod.POST, entity, Customer.class);
        var id = response1.getBody().getId();
        HttpEntity<Customer> customerDto = new HttpEntity<>(null, headers);
        //Act
        ResponseEntity<Customer> response2 = restTemplate.exchange(createURLWithPort("/api/v1/customers/" + id), HttpMethod.GET, customerDto, Customer.class);
        //Assert
        assertThat(response2.getBody().getId().equals(id));
    }

    @Test
    public void should_be_able_to_edit_a_customer() {
        //Arrange
        Customer aCustomerDto = createACustomer();
        HttpEntity<Customer> entity = new HttpEntity<>(aCustomerDto, headers);
        ResponseEntity<Customer> response1 = restTemplate.exchange(createURLWithPort("/api/v1/customers"), HttpMethod.POST, entity, Customer.class);
        var id = response1.getBody().getId();
        HttpEntity<Customer> customerDto = new HttpEntity<>(null, headers);
        aCustomerDto.setFirstName("New First Name");
        aCustomerDto.setLastName("New Last Name");
        //Act
        ResponseEntity<Customer> response = restTemplate.exchange(createURLWithPort("/api/v1/customers/" + id), HttpMethod.PUT, customerDto, Customer.class);

        //Assert
        assertThat(response.getStatusCodeValue() == 201);
        assertThat(response.getBody().getFirstName() == "New First Name");

    }

    @Test
    public void should_be_able_to_delete_a_customer() {
        //Arrange
        Customer aCustomerDto = createACustomer();
        HttpEntity<Customer> entity = new HttpEntity<>(aCustomerDto, headers);
        ResponseEntity<Customer> response1 = restTemplate.exchange(createURLWithPort("/api/v1/customers"), HttpMethod.POST, entity, Customer.class);
        var id = response1.getBody().getId();
        HttpEntity<Customer> customerDto = new HttpEntity<>(null, headers);

        //Act
        restTemplate.exchange(createURLWithPort("/api/v1/customers/"+ id), HttpMethod.DELETE, customerDto, String.class);
        ResponseEntity<Customer> response = restTemplate.exchange(createURLWithPort("/api/v1/customers/" + id), HttpMethod.GET, customerDto, Customer.class);

        //Assert
        assertThat(response.getStatusCodeValue() == 400);
        assertThat(response.getBody() == null);

    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}