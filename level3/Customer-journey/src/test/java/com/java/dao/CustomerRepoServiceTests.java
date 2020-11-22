package com.java.dao;

import com.java.exceptions.NotFoundException;
import com.java.exceptions.UnableToDeleteException;
import com.java.exceptions.UnableToGetException;
import com.java.exceptions.UnableToSaveException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Optional;

import static com.java.testUtils.TestUtilsFactory.aRandomCustomer;
import static com.java.testUtils.TestUtilsFactory.aRandomLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

public class CustomerRepoServiceTests {

    private final CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
    private final CustomerRepoService customerRepoService = new CustomerRepoService(customerRepository);

    @Test
    public void getAllCustomers_returnsAllSavedCustomers_whenSuccessfullyFetchedFromRepository() {
        Customer existingCustomer1InRepo = aRandomCustomer();
        Customer existingCustomer2InRepo = aRandomCustomer();
        Iterable<Customer> savedCustomers = Arrays.asList(existingCustomer1InRepo, existingCustomer2InRepo);
        Mockito.when(customerRepository.findAll()).thenReturn(savedCustomers);

        Iterable<Customer> savedCustomersRetrieved = customerRepoService.getAllCustomers();

        assertThat(savedCustomersRetrieved).isEqualTo(savedCustomers);
    }

    @Test
    public void getAllCustomers_throwsUnableToGetException_whenRepositoryThrowsExceptionWhileRetrieving() {
        String errorMessage = "Error message";
        doThrow(new RuntimeException(errorMessage)).when(customerRepository).findAll();

        UnableToGetException exceptionThrown = assertThrows(
                UnableToGetException.class,
                customerRepoService::getAllCustomers
        );

        assertThat(exceptionThrown.getMessage()).isEqualTo(String.format("Unable to get the object. Error: %s", errorMessage));
    }

    @Test
    public void getCustomerById_returnsCustomer_whenSuccessfullyRetrievedFromRepository() {
        Customer existingCustomerInRepo = aRandomCustomer();
        Long customerId = aRandomLong();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomerInRepo));

        Customer savedCustomerRetrieved = customerRepoService.getCustomerById(customerId);

        assertThat(savedCustomerRetrieved).isEqualTo(existingCustomerInRepo);
    }

    @Test
    public void getCustomerById_throwsUnableToGetException_whenCustomerIdIsNull() {
        UnableToGetException exceptionThrown = assertThrows(
                UnableToGetException.class,
                () -> customerRepoService.getCustomerById(null)
        );

        assertThat(exceptionThrown.getMessage()).isEqualTo("" +
                "Unable to get the object. Error: Customer ID must not be null. Please provide a valid customer ID.");
    }

    @Test
    public void getCustomerById_throwsNotFoundException_whenUnableToFindInRepository() {
        Long customerId = aRandomLong();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        NotFoundException exceptionThrown = assertThrows(
                NotFoundException.class,
                () -> customerRepoService.deleteExistingCustomer(customerId)
        );

        assertThat(exceptionThrown.getMessage()).isEqualTo(String.format("Object with ID %s not found! Please try a valid ID.", customerId));
    }

    @Test
    public void saveNewCustomer_returnsSavedCustomer_whenSuccessfullySavedInRepository() {
        ArgumentCaptor<Customer> argument = ArgumentCaptor.forClass(Customer.class);
        Customer customerToSave = aRandomCustomer();
        Mockito.when(customerRepository.save(any(Customer.class))).thenAnswer(i -> i.getArguments()[0]);

        Customer savedCustomer = customerRepoService.saveNewCustomer(customerToSave);

        Mockito.verify(customerRepository).save(argument.capture());
        assertThat(argument.getValue()).isEqualTo(customerToSave);
        assertThat(savedCustomer).isEqualTo(customerToSave);
    }

    @Test
    public void saveNewCustomer_throwsUnableToSaveException_whenCustomerToSaveIsNull() {
        UnableToSaveException exceptionThrown = assertThrows(
                UnableToSaveException.class,
                () -> customerRepoService.saveNewCustomer(null)
        );

        assertThat(exceptionThrown.getMessage())
                .isEqualTo("Unable to save the object. Error: Customer must not be null. Please provide a valid Customer object.");
    }

    @Test
    public void saveNewCustomer_throwsUnableToSaveException_whenIdIsNotNull() {
        Customer customerToSave = aRandomCustomer();
        ReflectionTestUtils.setField(customerToSave, "id", aRandomLong());

        UnableToSaveException exceptionThrown = assertThrows(
                UnableToSaveException.class,
                () -> customerRepoService.saveNewCustomer(customerToSave)
        );

        assertThat(exceptionThrown.getMessage())
                .isEqualTo("Unable to save the object. Error: ID must be null. Please provide Customer details without an ID.");
    }

    @Test
    public void saveNewCustomer_throwsUnableToSaveException_whenRepositoryThrowsException() {
        Customer customerToSave = aRandomCustomer();
        String errorMessage = "Error message";
        Mockito.when(customerRepository.save(any(Customer.class))).thenThrow(new RuntimeException(errorMessage));

        UnableToSaveException exceptionThrown = assertThrows(
                UnableToSaveException.class,
                () -> customerRepoService.saveNewCustomer(customerToSave)
        );

        assertThat(exceptionThrown.getMessage()).isEqualTo(String.format("Unable to save the object. Error: %s", errorMessage));
    }

    @Test
    public void updateExistingCustomer_returnsUpdatedCustomer_whenSuccessfullyUpdatedInRepository() {
        Customer existingCustomerInRepo = aRandomCustomer();
        Long customerId = aRandomLong();

        ArgumentCaptor<Customer> customerSaveArgument = ArgumentCaptor.forClass(Customer.class);
        Customer customerToUpdate = aRandomCustomer();
        Customer expectedUpdatedCustomer = new Customer(customerToUpdate.getFirstName(), customerToUpdate.getLastName());

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomerInRepo));
        Mockito.when(customerRepository.save(any(Customer.class))).thenAnswer(i -> i.getArguments()[0]);

        Customer actualUpdatedCustomer = customerRepoService.updateExistingCustomer(customerId, customerToUpdate);

        Mockito.verify(customerRepository).save(customerSaveArgument.capture());
        assertCustomerObjectFieldsEqual(customerSaveArgument.getValue(), expectedUpdatedCustomer);
        assertCustomerObjectFieldsEqual(actualUpdatedCustomer, expectedUpdatedCustomer);
    }

    @Test
    public void updateExistingCustomer_throwsUnableToSaveException_whenCustomerIdIsNull() {
        Customer customerToUpdate = aRandomCustomer();

        UnableToSaveException exceptionThrown = assertThrows(
                UnableToSaveException.class,
                () -> customerRepoService.updateExistingCustomer(null, customerToUpdate)
        );

        assertThat(exceptionThrown.getMessage())
                .isEqualTo("Unable to save the object. Error: Customer ID and customer details must not be null. " +
                        "Please provide a valid customer ID and Customer details.");
    }

    @Test
    public void updateExistingCustomer_throwsUnableToSaveException_whenCustomerIsNull() {
        Long customerId = aRandomLong();

        UnableToSaveException exceptionThrown = assertThrows(
                UnableToSaveException.class,
                () -> customerRepoService.updateExistingCustomer(customerId, null)
        );

        assertThat(exceptionThrown.getMessage())
                .isEqualTo("Unable to save the object. Error: Customer ID and customer details must not be null. " +
                        "Please provide a valid customer ID and Customer details.");
    }

    @Test
    public void updateExistingCustomer_throwsNotFoundException_whenUnableToFindInRepository() {
        Customer customerToUpdate = aRandomCustomer();
        Long customerId = aRandomLong();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        NotFoundException exceptionThrown = assertThrows(
                NotFoundException.class,
                () -> customerRepoService.updateExistingCustomer(customerId, customerToUpdate)
        );

        assertThat(exceptionThrown.getMessage()).isEqualTo(String.format("Object with ID %s not found! Please try a valid ID.", customerId));
    }

    @Test
    public void updateExistingCustomer_throwsUnableToSaveException_whenRepositoryThrowsExceptionWhileSaving() {
        Customer existingCustomerInRepo = aRandomCustomer();
        Long customerId = aRandomLong();

        Customer customerToUpdate = aRandomCustomer();
        String errorMessage = "Error message";
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomerInRepo));
        Mockito.when(customerRepository.save(any(Customer.class))).thenThrow(new RuntimeException(errorMessage));

        UnableToSaveException exceptionThrown = assertThrows(
                UnableToSaveException.class,
                () -> customerRepoService.updateExistingCustomer(customerId, customerToUpdate)
        );

        assertThat(exceptionThrown.getMessage()).isEqualTo(String.format("Unable to save the object. Error: %s", errorMessage));
    }

    @Test
    public void deleteExistingCustomer_doesNotThrowException_whenSuccessfullyDeletedFromRepository() {
        Customer existingCustomerInRepo = aRandomCustomer();
        Long customerId = aRandomLong();

        ArgumentCaptor<Customer> customerDeleteArgument = ArgumentCaptor.forClass(Customer.class);
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomerInRepo));
        doNothing().when(customerRepository).delete(any(Customer.class));

        assertDoesNotThrow(() -> customerRepoService.deleteExistingCustomer(customerId));

        Mockito.verify(customerRepository).delete(customerDeleteArgument.capture());
        assertThat(customerDeleteArgument.getValue()).isEqualTo(existingCustomerInRepo);
    }

    @Test
    public void deleteExistingCustomer_throwsUnableToDeleteException_whenCustomerIdIsNull() {
        UnableToDeleteException exceptionThrown = assertThrows(
                UnableToDeleteException.class,
                () -> customerRepoService.deleteExistingCustomer(null)
        );

        assertThat(exceptionThrown.getMessage()).isEqualTo("" +
                "Unable to delete the object. Error: Customer ID must not be null. Please provide a valid customer ID.");
    }

    @Test
    public void deleteExistingCustomer_throwsNotFoundException_whenUnableToFindInRepository() {
        Long customerId = aRandomLong();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        NotFoundException exceptionThrown = assertThrows(
                NotFoundException.class,
                () -> customerRepoService.deleteExistingCustomer(customerId)
        );

        assertThat(exceptionThrown.getMessage()).isEqualTo(String.format("Object with ID %s not found! Please try a valid ID.", customerId));
    }

    @Test
    public void deleteExistingCustomer_throwsUnableToDeleteException_whenRepositoryThrowsExceptionWhileDeleting() {
        Customer existingCustomerInRepo = aRandomCustomer();
        Long customerId = aRandomLong();

        String errorMessage = "Error message";
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomerInRepo));
        doThrow(new RuntimeException(errorMessage)).when(customerRepository).delete(any(Customer.class));

        UnableToDeleteException exceptionThrown = assertThrows(
                UnableToDeleteException.class,
                () -> customerRepoService.deleteExistingCustomer(customerId)
        );

        assertThat(exceptionThrown.getMessage()).isEqualTo(String.format("Unable to delete the object. Error: %s", errorMessage));
    }

    private void assertCustomerObjectFieldsEqual(Customer actualCustomer, Customer expectedCustomer) {
        assertThat(actualCustomer.getId()).isEqualTo(expectedCustomer.getId());
        assertThat(actualCustomer.getFirstName()).isEqualTo(expectedCustomer.getFirstName());
        assertThat(actualCustomer.getLastName()).isEqualTo(expectedCustomer.getLastName());
    }

}
