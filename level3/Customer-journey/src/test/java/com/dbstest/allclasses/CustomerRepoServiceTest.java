package com.dbstest.allclasses;

import com.dbstest.TestApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
/**
 * @author Henry
 */
@SpringBootTest(classes = TestApplication.class)
public class CustomerRepoServiceTest {

    @Autowired
    CustomerRepoService customerRepoService;
    @Autowired
    CustomerDAO customerDAO;

    Customer newCustomer;

    @BeforeEach
    void beforeEach() {
        newCustomer = new Customer();
        newCustomer.setId("T7654321C");
        newCustomer.setName("New Customer");
        newCustomer.setGender(Gender.MALE);
        newCustomer.setDateOfBirth(20000101);

    }

    @Test
    @Sql(scripts = {"classpath:/scripts/insert-customer-script.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/scripts/truncate-customer.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void When_saving_a_new_customer_not_exist_in_database_Expect_success() throws AppException {
        customerRepoService.saveCustomer(newCustomer);
        Assertions.assertEquals(2, customerDAO.count());
    }

    @Test
    @Sql(scripts = {"classpath:/scripts/insert-customer-script.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/scripts/truncate-customer.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void When_saving_an_already_existing_customer_in_database_again_Expect_throw_AppException() throws AppException {
        newCustomer.setId("S1234567A");
        try {
            customerRepoService.saveCustomer(newCustomer);
        } catch (AppException appEx) {
            Assertions.assertEquals("Customer already exist, cannot overwrite",
                    appEx.getMessage());
        }
    }

    @Test
    @Sql(scripts = {"classpath:/scripts/insert-customer-script.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/scripts/truncate-customer.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void When_updating_a_customer_not_exist_in_database_Expect_Expect_throw_AppException() throws AppException {
        try {
            customerRepoService.updateCustomer(newCustomer);
        } catch (AppException appEx) {
            Assertions.assertEquals("Customer does not exist, unable to update",
                    appEx.getMessage());
        }
    }

    @Test
    @Sql(scripts = {"classpath:/scripts/insert-customer-script.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/scripts/truncate-customer.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void When_updating_an_existing_customer_in_database_Expect_Success() throws AppException {
        newCustomer.setId("S1234567A");
        customerRepoService.updateCustomer(newCustomer);
        Assertions.assertEquals("New Customer",
                customerDAO.findById("S1234567A").get().getName());
    }

    @Test
    @Sql(scripts = {"classpath:/scripts/insert-customer-script.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/scripts/truncate-customer.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void When_deleting_an_existing_customer_in_database_Expect_Success() throws AppException {
        Assertions.assertTrue(customerDAO.existsById("S1234567A"));
        customerRepoService.deleteCustomer("S1234567A");
        Assertions.assertFalse(customerDAO.existsById("S1234567A"));
    }

    @Test
    void When_deleting_a_non_existing_customer_in_database_Expect_throw_AppException() throws AppException {
        try {
            customerRepoService.deleteCustomer("S1234567A");
        } catch (AppException appEx) {
            Assertions.assertEquals("Customer not found, unable to delete", appEx.getMessage());
        }
    }

    @Test
    void When_getting_a_non_existing_customer_in_database_Expect_throw_AppException() throws AppException {
        try {
            customerRepoService.getCustomer("S1234567A");
        } catch (AppException appEx) {
            Assertions.assertEquals("Customer not found", appEx.getMessage());
        }
    }

    @Test
    @Sql(scripts = {"classpath:/scripts/insert-customer-script.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:/scripts/truncate-customer.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void When_getting_an_existing_customer_in_database_Expect_Success() throws AppException {
        Customer cust = customerRepoService.getCustomer("S1234567A");
        Assertions.assertEquals("S1234567A", cust.getId());
        Assertions.assertEquals("John Doe", cust.getName());
        Assertions.assertEquals(Gender.MALE, cust.getGender());
        Assertions.assertEquals(19901225, cust.getDateOfBirth());
    }
}