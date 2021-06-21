package com.customer.journey.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.customer.journey.domain.Customer;
import com.customer.journey.exception.ApplicationException;
import com.customer.journey.service.ICustomerRepoService;
import com.customer.journey.util.CustomerConstants;
import com.customer.journey.validation.CheckCustomerId;
import com.customer.journey.validation.CheckCustomerList;
import com.customer.journey.validation.CustomerValidator;

/**
 * @author Jhonson
 *
 */
@RestController
@RequestMapping(CustomerConstants.CONTR_PATH_ROOT)
@Validated
public class CustomerController {

	@Autowired
	private ICustomerRepoService customerRepoService;

	@Autowired
	private CustomerValidator customerValidator;

	@InitBinder(CustomerConstants.CUST_BINDER)
	public void initMerchantOnlyBinder(WebDataBinder binder) {
		binder.addValidators(customerValidator);
	}

	/**
	 * @return
	 * @throws ApplicationException
	 * This method will return all customers from repository 
	 */
	@GetMapping(value = CustomerConstants.CONTR_PATH_LIST_ALL_CUST)
	public ResponseEntity<List<Customer>> getAllCustomer() throws ApplicationException {

		return new ResponseEntity<List<Customer>>((customerRepoService.getAllCustomers()), HttpStatus.OK);

	}

	/**
	 * @param customerId
	 * @return
	 * @throws ApplicationException
	 * This method will return a customer by customer Id. @CheckCustomerId constraint will be applied to this method.
	 */
	@GetMapping(value = CustomerConstants.CONTR_PATH_FIND_BY_ID)
	@ResponseBody
	public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") @CheckCustomerId String customerId)
			throws ApplicationException {

		return new ResponseEntity<Customer>((customerRepoService.findById(Long.valueOf(customerId))), HttpStatus.OK);

	}

	/**
	 * @param pCustomer
	 * @return
	 * @throws ApplicationException
	 * This method will handle customer creation into repository. 
	 * This method will make sure of custom validator to validate the customer's fields. 
	 */
	@PostMapping(value = CustomerConstants.CONTR_PATH_CREATE_CUST)
	@ResponseBody
	public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer pCustomer) throws ApplicationException {

		return new ResponseEntity<Customer>(customerRepoService.addCustomer(pCustomer), HttpStatus.CREATED);
	}

	/**
	 * @param pCustomer
	 * @return
	 * @throws ApplicationException
	 * This method will handle customer update into repository. 
	 * This method will make sure of custom validator to validate the customer's fields.
	 */
	@PutMapping(value = CustomerConstants.CONTR_PATH_UPDATE_CUST)
	@ResponseBody
	public ResponseEntity<Customer> updateCustomer(@Valid @RequestBody Customer pCustomer) throws ApplicationException {

		return new ResponseEntity<Customer>(customerRepoService.updateCustomer(pCustomer), HttpStatus.CREATED);
	}

	/**
	 * @param pCustomer
	 * @return
	 * @throws ApplicationException
	 * This method will handle customer deletion from repository. 
	 * This method will make sure of custom validator to validate the customer's fields.
	 */
	@DeleteMapping(value = CustomerConstants.CONTR_PATH_DELETE_CUST)
	@ResponseBody
	public ResponseEntity<Customer> deleteCustomer(@Valid @RequestBody Customer pCustomer) throws ApplicationException {

		return new ResponseEntity<Customer>(customerRepoService.deleteCustomer(pCustomer), HttpStatus.CREATED);

	}

	/**
	 * @param pCustomers
	 * @return
	 * @throws ApplicationException
	 * This method will handle a group of customers creation into repository. 
	 * @CheckCustomerList constraint will be applied to this method
	 */
	@PostMapping(value = CustomerConstants.CONTR_PATH_CREATE_ALL_CUST)
	@ResponseBody
	public ResponseEntity<List<Customer>> createAllCustomer(@RequestBody @CheckCustomerList List<Customer> pCustomers)
			throws ApplicationException {

		return new ResponseEntity<List<Customer>>(customerRepoService.addAllCustomer(pCustomers), HttpStatus.CREATED);
	}

	/**
	 * @param pCustomers
	 * @return
	 * @throws ApplicationException
	 * This method will handle a group of customers update into repository. 
	 * @CheckCustomerList constraint will be applied to this method
	 */
	@PutMapping(value = CustomerConstants.CONTR_PATH_UPDATE_ALL_CUST)
	@ResponseBody
	public ResponseEntity<List<Customer>> updateAllCustomer(
			@Valid @RequestBody @CheckCustomerList List<Customer> pCustomers) throws ApplicationException {

		return new ResponseEntity<List<Customer>>(customerRepoService.updateAllCustomer(pCustomers),
				HttpStatus.CREATED);
	}

	/**
	 * @param pCustomers
	 * @return
	 * @throws ApplicationException
	 * This method will handle a group of customers deletion from repository. 
	 * @CheckCustomerList constraint will be applied to this method
	 */
	@DeleteMapping(value = CustomerConstants.CONTR_PATH__DELETE_ALL_CUST)
	@ResponseBody
	public ResponseEntity<List<Customer>> deleteAllCustomer(@RequestBody @CheckCustomerList List<Customer> pCustomers)
			throws ApplicationException {

		return new ResponseEntity<List<Customer>>(customerRepoService.deleteAllCustomer(pCustomers),
				HttpStatus.CREATED);

	}

}
