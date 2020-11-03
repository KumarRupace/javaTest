package com.java.dao;

import com.java.app.AccessingDataJpaApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

@Transactional
@SpringBootTest(classes = AccessingDataJpaApplication.class)
public class CustomerRepoServiceTest {

    private static final String CUSTOMER_ID_2_FIRST_NAME = "Wei Cheng";
    private static final String CUSTOMER_ID_2_LAST_NAME = "Kow";

    private static final String NEW_CUSTOMER_FIRST_NAME = "Jun Wei";
    private static final String NEW_CUSTOMER_LAST_NAME = "Chang";

    private static final String VERY_LONG_FIRST_NAME = "Anderson Jason Andrew James Samuel";
    private static final String VERY_LONG_LAST_NAME = "Chang Chi Yoong Teo Zhang";

    @Autowired
    private CustomerRepoService mCustomerRepoService;

    @Autowired
    private CustomerRepository mCustomerRepository;

    @Test
    public void testCreateCustomerRecord_WhenFirstNameIsNull_ShouldThrowDataIntegrityViolationException() {
        Customer customer = new Customer(null, NEW_CUSTOMER_LAST_NAME);

        Throwable thrown = catchThrowable(() -> {
            mCustomerRepoService.save(customer);
        });

        assertThat(thrown)
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void testCreateCustomerRecord_WhenLastNameIsNull_ShouldThrowDataIntegrityViolationException() {
        Customer customer = new Customer(NEW_CUSTOMER_FIRST_NAME, null);

        Throwable thrown = catchThrowable(() -> {
            mCustomerRepoService.save(customer);
        });

        assertThat(thrown)
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    public void testCreateCustomerRecord_WithVeryLongFirstName_ShouldThrowJpaSystemException() {
        Customer customer = new Customer(VERY_LONG_FIRST_NAME, NEW_CUSTOMER_LAST_NAME);

        Throwable thrown = catchThrowable(() -> {
            mCustomerRepoService.save(customer);
        });

        assertThat(thrown)
            .isInstanceOf(JpaSystemException.class);
    }

    @Test
    public void testCreateCustomerRecord_WithVeryLongLastName_ShouldThrowJpaSystemException() {
        Customer customer = new Customer(NEW_CUSTOMER_FIRST_NAME, VERY_LONG_LAST_NAME);

        Throwable thrown = catchThrowable(() -> {
            mCustomerRepoService.save(customer);
        });

        assertThat(thrown)
                .isInstanceOf(JpaSystemException.class);
    }

    @Test
    public void testCreateCustomerRecord() {
        Customer customer = new Customer(NEW_CUSTOMER_FIRST_NAME, NEW_CUSTOMER_LAST_NAME);

        mCustomerRepoService.save(customer);

        Optional<Customer> result = mCustomerRepository.findById(customer.getId());
        Customer inserted;

        assertTrue(result.isPresent());
        assertNotNull(inserted = result.get());
        assertThat(inserted.getFirstName()).isEqualTo(NEW_CUSTOMER_FIRST_NAME);
        assertThat(inserted.getLastName()).isEqualTo(NEW_CUSTOMER_LAST_NAME);
    }

    @Test
    public void testUpdateCustomerRecord() {
        Customer customer = mCustomerRepository.findById(2);
        customer.setFirstName(NEW_CUSTOMER_FIRST_NAME);

        mCustomerRepoService.save(customer);

        Optional<Customer> result = mCustomerRepository.findById(customer.getId());
        Customer updated;

        assertTrue(result.isPresent());
        assertNotNull(updated = result.get());
        assertThat(updated.getId()).isEqualTo(customer.getId());
        assertThat(updated.getFirstName()).isEqualTo(NEW_CUSTOMER_FIRST_NAME);
        assertThat(updated.getLastName()).isEqualTo(CUSTOMER_ID_2_LAST_NAME);
    }


    @Test
    public void testRetrieveCustomerRecord_WithNonExistenceId_ShouldReturnNull() {
        assertNull(mCustomerRepoService.retrieve(100));
    }

    @Test
    public void testRetrieveCustomerRecord() {
        Customer customer = mCustomerRepoService.retrieve(2);

        assertNotNull(customer);
        assertThat(customer.getFirstName()).isEqualTo(CUSTOMER_ID_2_FIRST_NAME);
        assertThat(customer.getLastName()).isEqualTo(CUSTOMER_ID_2_LAST_NAME);
    }

    @Test
    public void testDeleteCustomerRecord() {
        long customerId = 1;

        assertNotNull(mCustomerRepository.findById(customerId));

        mCustomerRepoService.delete(customerId);

        assertNull(mCustomerRepository.findById(customerId));
    }

    @Test
    public void testExists() {
        // Where Record Exist in Database
        assertTrue(mCustomerRepoService.exists(3));

        // Where Record Does NOT Exist in Database
        assertFalse(mCustomerRepoService.exists(20));
    }

}
