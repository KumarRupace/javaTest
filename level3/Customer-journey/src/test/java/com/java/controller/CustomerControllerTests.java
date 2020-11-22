package com.java.controller;

import com.java.app.AccessingDataJpaApplication;
import com.java.dao.Customer;
import com.java.dao.CustomerRepoService;
import com.java.exceptions.NotFoundException;
import com.java.exceptions.UnableToDeleteException;
import com.java.exceptions.UnableToGetException;
import com.java.exceptions.UnableToSaveException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static com.java.testUtils.TestUtilsFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = AccessingDataJpaApplication.class)
public class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepoService customerRepoService;

    private final String customerControllerBaseUrl = "/customers";

    @Test
    public void getAllEndpoint_returnsAllSavedCustomersAndStatusOk_whenAllCustomersRetrieved() throws Exception {
        Iterable<Customer> savedCustomers = Arrays.asList(aRandomCustomer(), aRandomCustomer());
        String savedCustomersJson = getObjectMapper().writeValueAsString(savedCustomers);
        Mockito.when(customerRepoService.getAllCustomers()).thenReturn(savedCustomers);

        MvcResult result = mockMvc.perform(get(customerControllerBaseUrl))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).isEqualTo(savedCustomersJson);
    }

    @Test
    public void getAllEndpoint_throwsUnableToGetException_whenErrorWhileRetrievingAllCustomers() throws Exception {
        String errorMessage = "Error message";
        Mockito.when(customerRepoService.getAllCustomers())
                .thenThrow(new UnableToGetException(errorMessage));

        mockMvc.perform(get(customerControllerBaseUrl))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UnableToGetException.class))
                .andExpect(result -> assertThat(result
                        .getResolvedException()
                        .getMessage()).isEqualTo(String.format("Unable to get the object. Error: %s", errorMessage)));
    }

    @Test
    public void getByIdEndpoint_returnsSavedCustomerAndStatusOk_whenCustomerRetrievedById() throws Exception {
        Customer savedCustomer = aRandomCustomer();
        Long customerId = aRandomLong();
        String savedCustomerJson = getObjectMapper().writeValueAsString(savedCustomer);
        Mockito.when(customerRepoService.getCustomerById(customerId)).thenReturn(savedCustomer);

        MvcResult result = mockMvc.perform(get(String.format("%s/%s", customerControllerBaseUrl, customerId)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).isEqualTo(savedCustomerJson);
    }

    @Test
    public void getByIdEndpoint_throwsUnableToGetExceptionWithMessage_whenServiceThrowsUnableToGetException() throws Exception {
        Long customerId = aRandomLong();
        String errorMessage = "Error message";
        doThrow(new UnableToGetException(errorMessage)).when(customerRepoService).getCustomerById(anyLong());

        mockMvc.perform(get(String.format("%s/%s", customerControllerBaseUrl, customerId)))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UnableToGetException.class))
                .andExpect(result -> assertThat(result
                        .getResolvedException()
                        .getMessage()).isEqualTo(String.format("Unable to get the object. Error: %s", errorMessage)));
    }

    @Test
    public void getByIdEndpoint_throwsNotFoundExceptionWithMessage_whenServiceThrowsNotFoundException() throws Exception {
        Long customerId = aRandomLong();
        doThrow(new NotFoundException(customerId)).when(customerRepoService).getCustomerById(anyLong());

        mockMvc.perform(get(String.format("%s/%s", customerControllerBaseUrl, customerId)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(NotFoundException.class))
                .andExpect(result -> assertThat(result
                        .getResolvedException()
                        .getMessage()).isEqualTo(String.format("Object with ID %s not found! Please try a valid ID.", customerId)));
    }

    @Test
    public void postEndpoint_returnsSavedCustomerAndStatusCreated_whenCustomerCreated() throws Exception {
        Customer customerToSave = aRandomCustomer();
        String customerToSaveJson = getObjectMapper().writeValueAsString(customerToSave);
        Mockito.when(customerRepoService.saveNewCustomer(any(Customer.class))).thenAnswer(i -> i.getArguments()[0]);

        MvcResult result = mockMvc.perform(post(customerControllerBaseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerToSaveJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).isEqualTo(customerToSaveJson);
    }

    @Test
    public void postEndpoint_throwsBadRequest_whenCustomerFirstNameIsNull() throws Exception {
        Customer customerToSave = aRandomCustomer();
        customerToSave.setFirstName(null);
        String customerToSaveJson = getObjectMapper().writeValueAsString(customerToSave);

        mockMvc.perform(post(customerControllerBaseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerToSaveJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postEndpoint_throwsBadRequest_whenCustomerLastNameIsNull() throws Exception {
        Customer customerToSave = aRandomCustomer();
        customerToSave.setLastName(null);
        String customerToSaveJson = getObjectMapper().writeValueAsString(customerToSave);

        mockMvc.perform(post(customerControllerBaseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerToSaveJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postEndpoint_throwsUnableToSaveExceptionWithMessage_whenServiceThrowsUnableToSaveException() throws Exception {
        Customer customerToSave = aRandomCustomer();
        String customerToSaveJson = getObjectMapper().writeValueAsString(customerToSave);
        String errorMessage = "Error message";
        Mockito.when(customerRepoService.saveNewCustomer(any(Customer.class)))
                .thenThrow(new UnableToSaveException(errorMessage));

        mockMvc.perform(post(customerControllerBaseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerToSaveJson))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UnableToSaveException.class))
                .andExpect(result -> assertThat(result
                        .getResolvedException()
                        .getMessage()).isEqualTo(String.format("Unable to save the object. Error: %s", errorMessage)));
    }

    @Test
    public void putEndpoint_returnsUpdatedCustomerAndStatusOk_whenCustomerUpdated() throws Exception {
        Customer customerToSave = aRandomCustomer();
        String customerToSaveJson = getObjectMapper().writeValueAsString(customerToSave);
        Long customerId = aRandomLong();
        Mockito.when(customerRepoService.updateExistingCustomer(anyLong(), any(Customer.class)))
                .thenAnswer(i -> i.getArguments()[1]);

        MvcResult result = mockMvc.perform(put(String.format("%s/%s", customerControllerBaseUrl, customerId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerToSaveJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).isEqualTo(customerToSaveJson);
    }

    @Test
    public void putEndpoint_throwsBadRequest_whenCustomerFirstNameIsNull() throws Exception {
        Customer customerToSave = aRandomCustomer();
        customerToSave.setFirstName(null);
        Long customerId = aRandomLong();
        String customerToSaveJson = getObjectMapper().writeValueAsString(customerToSave);

        mockMvc.perform(put(String.format("%s/%s", customerControllerBaseUrl, customerId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerToSaveJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void putEndpoint_throwsBadRequest_whenCustomerLastNameIsNull() throws Exception {
        Customer customerToSave = aRandomCustomer();
        customerToSave.setLastName(null);
        Long customerId = aRandomLong();
        String customerToSaveJson = getObjectMapper().writeValueAsString(customerToSave);

        mockMvc.perform(put(String.format("%s/%s", customerControllerBaseUrl, customerId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerToSaveJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void putEndpoint_throwsUnableToSaveExceptionWithMessage_whenServiceThrowsUnableToSaveException() throws Exception {
        Customer customerToSave = aRandomCustomer();
        Long customerId = aRandomLong();
        String customerToSaveJson = getObjectMapper().writeValueAsString(customerToSave);
        String errorMessage = "Error message";
        Mockito.when(customerRepoService.updateExistingCustomer(anyLong(), any(Customer.class)))
                .thenThrow(new UnableToSaveException(errorMessage));

        mockMvc.perform(put(String.format("%s/%s", customerControllerBaseUrl, customerId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerToSaveJson))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UnableToSaveException.class))
                .andExpect(result -> assertThat(result
                        .getResolvedException()
                        .getMessage()).isEqualTo(String.format("Unable to save the object. Error: %s", errorMessage)));
    }

    @Test
    public void putEndpoint_throwsNotFoundExceptionWithMessage_whenServiceThrowsNotFoundException() throws Exception {
        Customer customerToSave = aRandomCustomer();
        Long customerId = aRandomLong();
        String customerToSaveJson = getObjectMapper().writeValueAsString(customerToSave);
        Mockito.when(customerRepoService.updateExistingCustomer(anyLong(), any(Customer.class)))
                .thenThrow(new NotFoundException(customerId));

        mockMvc.perform(put(String.format("%s/%s", customerControllerBaseUrl, customerId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerToSaveJson))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(NotFoundException.class))
                .andExpect(result -> assertThat(result
                        .getResolvedException()
                        .getMessage()).isEqualTo(String.format("Object with ID %s not found! Please try a valid ID.", customerId)));
    }

    @Test
    public void deleteEndpoint_returnsSuccessMessageAndStatusOk_whenCustomerDeleted() throws Exception {
        Long customerId = aRandomLong();
        doNothing().when(customerRepoService).deleteExistingCustomer(anyLong());

        MvcResult result = mockMvc.perform(delete(String.format("%s/%s", customerControllerBaseUrl, customerId)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).isEqualTo("Customer successfully deleted");
    }

    @Test
    public void deleteEndpoint_throwsUnableToDeleteExceptionWithMessage_whenServiceThrowsUnableToDeleteException() throws Exception {
        Long customerId = aRandomLong();
        String errorMessage = "Error message";
        doThrow(new UnableToDeleteException(errorMessage)).when(customerRepoService).deleteExistingCustomer(anyLong());

        mockMvc.perform(delete(String.format("%s/%s", customerControllerBaseUrl, customerId)))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UnableToDeleteException.class))
                .andExpect(result -> assertThat(result
                        .getResolvedException()
                        .getMessage()).isEqualTo(String.format("Unable to delete the object. Error: %s", errorMessage)));
    }

    @Test
    public void deleteEndpoint_throwsNotFoundExceptionWithMessage_whenServiceThrowsNotFoundException() throws Exception {
        Long customerId = aRandomLong();
        doThrow(new NotFoundException(customerId)).when(customerRepoService).deleteExistingCustomer(anyLong());

        mockMvc.perform(delete(String.format("%s/%s", customerControllerBaseUrl, customerId)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(NotFoundException.class))
                .andExpect(result -> assertThat(result
                        .getResolvedException()
                        .getMessage()).isEqualTo(String.format("Object with ID %s not found! Please try a valid ID.", customerId)));
    }
}
