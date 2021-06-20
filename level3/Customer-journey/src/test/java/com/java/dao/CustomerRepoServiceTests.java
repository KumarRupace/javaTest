package com.java.dao;

import com.java.exception.EntityNotFoundException;
import com.java.exception.UnableToDeleteException;
import com.java.exception.UnableToGetException;
import com.java.exception.UnableToSaveException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Optional;

import static com.java.helper.TestHelper.createACustomer;
import static com.java.helper.TestHelper.createRandomLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

public class CustomerRepoServiceTests {

    private final CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
    private final CustomerRepoService customerRepoService = new CustomerRepoService(customerRepository);

    @Test
    public void get_AllCustomers_returns_AllSavedCustomers_whenSuccessfullyFetchedFromRepository() {
        Customer existingCustomer1InRepo = createACustomer();
        Customer existingCustomer2InRepo = createACustomer();
        Iterable<Customer> savedCustomers = Arrays.asList(existingCustomer1InRepo, existingCustomer2InRepo);
        Mockito.when(customerRepository.findAll()).thenReturn(savedCustomers);
        Iterable<Customer> savedCustomersRetrieved = customerRepoService.getAllCustomers();
        assertThat(savedCustomersRetrieved).isEqualTo(savedCustomers);
    }

    @Test
    public void getAllCustomers_throwsUnableToGetException_whenRepositoryThrowsExceptionWhileRetrieving() {
        String errorMessage = "Test Runtime exception with find all";
        doThrow(new RuntimeException(errorMessage)).when(customerRepository).findAll();

        RuntimeException exceptionThrown = assertThrows(
                RuntimeException.class,
                customerRepoService::getAllCustomers
        );
        assertThat(exceptionThrown.getMessage()).isEqualTo("Test Runtime exception with find all");
    }

    @Test
    public void getCustomerById_returnsCustomer_whenSuccessfullyRetrievedFromRepository() {
        Customer existingCustomerInRepo = createACustomer();
        Long customerId = createRandomLong();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomerInRepo));

        Customer savedCustomerRetrieved = customerRepoService.retrieveCustomerById(customerId);

        assertThat(savedCustomerRetrieved).isEqualTo(existingCustomerInRepo);
    }

    @Test
    public void getCustomerById_throwsUnableToGetException_whenCustomerIdIsNull() {
        UnableToGetException exceptionThrown = assertThrows(
                UnableToGetException.class,
                () -> customerRepoService.retrieveCustomerById(null)
        );

        assertThat(exceptionThrown.getMessage()).isEqualTo("" +
                "Unable to get the object. Error: Customer Id must not be null. Please provide a valid customer Id.");
    }

    @Test
    public void getCustomerById_throwsNotFoundException_whenUnableToFindInRepository() {
        Long customerId = createRandomLong();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        EntityNotFoundException exceptionThrown = assertThrows(
                EntityNotFoundException.class,
                () -> customerRepoService.deleteCustomer(customerId)
        );

        assertThat(exceptionThrown.getMessage()).isEqualTo(String.format("No customer found with id: %d Please try with a valid customer Id.", customerId));
    }

    @Test
    public void saveNewCustomer_returnsSavedCustomer_whenSuccessfullySavedInRepository() {
        ArgumentCaptor<Customer> argument = ArgumentCaptor.forClass(Customer.class);
        Customer customerToSave = createACustomer();
        Mockito.when(customerRepository.save(any(Customer.class))).thenAnswer(i -> i.getArguments()[0]);

        Customer savedCustomer = customerRepoService.createCustomer(customerToSave);

        Mockito.verify(customerRepository).save(argument.capture());
        assertThat(argument.getValue()).isEqualTo(customerToSave);
        assertThat(savedCustomer).isEqualTo(customerToSave);
    }

    @Test
    public void saveNewCustomer_throwsNullPointerException_whenCustomerToSaveIsNull() {
        NullPointerException exceptionThrown = assertThrows(
                NullPointerException.class,
                () -> customerRepoService.createCustomer(null)
        );

        assertThat(exceptionThrown.getMessage())
                .isEqualTo(null);
    }

    @Test
    public void saveNewCustomer_throwsUnableToSaveException_whenIdIsNotNull() {
        Customer customerToSave = createACustomer();
        ReflectionTestUtils.setField(customerToSave, "id", createRandomLong());

        UnableToSaveException exceptionThrown = assertThrows(
                UnableToSaveException.class,
                () -> customerRepoService.createCustomer(customerToSave)
        );

        assertThat(exceptionThrown.getMessage())
                .isEqualTo("Unable to save the object. Error: Id must be null. Please provide Customer details without an Id.");
    }

    @Test
    public void saveNewCustomer_throwsUnableToSaveException_whenRepositoryThrowsException() {
        Customer customerToSave = createACustomer();
        String errorMessage = "Error message";
        Mockito.when(customerRepository.save(any(Customer.class))).thenThrow(new RuntimeException(errorMessage));

        UnableToSaveException exceptionThrown = assertThrows(
                UnableToSaveException.class,
                () -> customerRepoService.createCustomer(customerToSave)
        );

        assertThat(exceptionThrown.getMessage()).isEqualTo(String.format("Unable to save the object. Error: %s", errorMessage));
    }

    @Test
    public void updateExistingCustomer_returnsUpdatedCustomer_whenSuccessfullyUpdatedInRepository() {
        Customer existingCustomerInRepo = createACustomer();
        Long customerId = createRandomLong();

        ArgumentCaptor<Customer> customerSaveArgument = ArgumentCaptor.forClass(Customer.class);
        Customer customerToUpdate = createACustomer();
        Customer expectedUpdatedCustomer = new Customer(customerToUpdate.getFirstName(), customerToUpdate.getLastName());

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomerInRepo));
        Mockito.when(customerRepository.save(any(Customer.class))).thenAnswer(i -> i.getArguments()[0]);

        Customer actualUpdatedCustomer = customerRepoService.updateCustomer(customerId, customerToUpdate);

        Mockito.verify(customerRepository).save(customerSaveArgument.capture());
        assertCustomerObjectFieldsEqual(customerSaveArgument.getValue(), expectedUpdatedCustomer);
        assertCustomerObjectFieldsEqual(actualUpdatedCustomer, expectedUpdatedCustomer);
    }

    @Test
    public void updateExistingCustomer_throwsUnableToSaveException_whenCustomerIdIsNull() {
        Customer customerToUpdate = createACustomer();

        UnableToSaveException exceptionThrown = assertThrows(
                UnableToSaveException.class,
                () -> customerRepoService.updateCustomer(null, customerToUpdate)
        );

        assertThat(exceptionThrown.getMessage())
                .isEqualTo("Unable to save the object. Error: Id must not be null. Please provide valid customer Id.");
    }

    @Test
    public void updateExistingCustomer_throwsUnableToSaveException_whenCustomerIsNull() {

        Long customerId = createRandomLong();
        UnableToSaveException exceptionThrown = assertThrows(
                UnableToSaveException.class,
                () -> customerRepoService.updateCustomer(customerId, null)
        );

        assertThat(exceptionThrown.getMessage())
                .isEqualTo("Unable to save the object. Error: Data must not be null. Please provide valid customer details.");
    }

    @Test
    public void updateExistingCustomer_throwsNotFoundException_whenUnableToFindInRepository() {
        Customer customerToUpdate = createACustomer();
        Long customerId = createRandomLong();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        EntityNotFoundException exceptionThrown = assertThrows(
                EntityNotFoundException.class,
                () -> customerRepoService.updateCustomer(customerId, customerToUpdate)
        );

        assertThat(exceptionThrown.getMessage()).isEqualTo(String.format("No customer found with id: %s Please try with a valid customer Id.", customerId));
    }

    @Test
    public void updateExistingCustomer_throwsUnableToSaveException_whenRepositoryThrowsExceptionWhileSaving() {
        Customer existingCustomerInRepo = createACustomer();
        Long customerId = createRandomLong();

        Customer customerToUpdate = createACustomer();
        String errorMessage = "Error message";
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomerInRepo));
        Mockito.when(customerRepository.save(any(Customer.class))).thenThrow(new RuntimeException(errorMessage));

        UnableToSaveException exceptionThrown = assertThrows(
                UnableToSaveException.class,
                () -> customerRepoService.updateCustomer(customerId, customerToUpdate)
        );

        assertThat(exceptionThrown.getMessage()).isEqualTo(String.format("Unable to save the object. Error: %s", errorMessage));
    }

    @Test
    public void deleteExistingCustomer_doesNotThrowException_whenSuccessfullyDeletedFromRepository() {
        Customer existingCustomerInRepo = createACustomer();
        Long customerId = createRandomLong();

        ArgumentCaptor<Customer> customerDeleteArgument = ArgumentCaptor.forClass(Customer.class);
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomerInRepo));
        doNothing().when(customerRepository).delete(any(Customer.class));

        assertDoesNotThrow(() -> customerRepoService.deleteCustomer(customerId));

        Mockito.verify(customerRepository).delete(customerDeleteArgument.capture());
        assertThat(customerDeleteArgument.getValue()).isEqualTo(existingCustomerInRepo);
    }

    @Test
    public void deleteExistingCustomer_throwsUnableToDeleteException_whenCustomerIdIsNull() {
        UnableToDeleteException exceptionThrown = assertThrows(
                UnableToDeleteException.class,
                () -> customerRepoService.deleteCustomer(null)
        );

        assertThat(exceptionThrown.getMessage()).isEqualTo("" +
                "Unable to delete the object. Error: Id must not be null. Please provide a valid customer Id.");
    }

    @Test
    public void deleteExistingCustomer_throwsNotFoundException_whenUnableToFindInRepository() {
        Long customerId = createRandomLong();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        EntityNotFoundException exceptionThrown = assertThrows(
                EntityNotFoundException.class,
                () -> customerRepoService.deleteCustomer(customerId)
        );

        assertThat(exceptionThrown.getMessage()).isEqualTo(String.format("No customer found with id: %s Please try with a valid customer Id.", customerId));
    }

    @Test
    public void deleteExistingCustomer_throwsUnableToDeleteException_whenRepositoryThrowsExceptionWhileDeleting() {
        Customer existingCustomerInRepo = createACustomer();
        Long customerId = createRandomLong();

        String errorMessage = "Error message";
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomerInRepo));
        doThrow(new RuntimeException(errorMessage)).when(customerRepository).delete(any(Customer.class));

        UnableToDeleteException exceptionThrown = assertThrows(
                UnableToDeleteException.class,
                () -> customerRepoService.deleteCustomer(customerId)
        );

        assertThat(exceptionThrown.getMessage()).isEqualTo(String.format("Unable to delete the object. Error: %s", errorMessage));
    }

    private void assertCustomerObjectFieldsEqual(Customer actualCustomer, Customer expectedCustomer) {
        assertThat(actualCustomer.getId()).isEqualTo(expectedCustomer.getId());
        assertThat(actualCustomer.getFirstName()).isEqualTo(expectedCustomer.getFirstName());
        assertThat(actualCustomer.getLastName()).isEqualTo(expectedCustomer.getLastName());
    }

}