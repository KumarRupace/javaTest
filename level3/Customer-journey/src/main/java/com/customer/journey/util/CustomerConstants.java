package com.customer.journey.util;

public interface CustomerConstants {
	
	String GET_ALLOWED_URI_LIST="${app.allowed.uri}";
	String CUST_NOT_FOUND="customer.not.found";
	String DELETE_FAIL="customer.delete.fail";
	String UPDATE_FAIL="customer.update.fail";	
	String INVALID_FIRSTNAME="FirstName.Invalid";
	String INVALID_LASTNAME="LastName.Invalid";	
	String RESOURCE_ACCESS_VIOLATION="resource.access.violation";
	String CUST_BINDER="customer";
	String CHK_CUST_ID_DEFAULT_MSG="{default.message.customerId.error}";
	String CHK_CUST_DATA_DEFAULT_MSG="{default.message.customer.error}";
	
	String CONTR_PATH_ROOT="/customer";	
	String CONTR_PATH_LIST_ALL_CUST="/listAllCustomers";
	String CONTR_PATH_FIND_BY_ID="/findById/{customerId}";
	String CONTR_PATH_CREATE_CUST="/create";
	String CONTR_PATH_UPDATE_CUST="/update";	
	String CONTR_PATH_DELETE_CUST="/delete";
	String CONTR_PATH_CREATE_ALL_CUST= "/createAll";
	String CONTR_PATH_UPDATE_ALL_CUST="/updateAll";
	String CONTR_PATH__DELETE_ALL_CUST="/deleteAll";
	String PATH_HAS_FIND_BY_ID="/customer/findById/";
	
	String ERR_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
	String ERR_MSG_GIVE_VALID_DATA="Enter.Valid.data";
	String ERR_MSG_GIVE_VALID_RESOURCE="Enter.Valid.resource";
	
	
	public static boolean checkInputString(String input) {
        return (input == null || input.trim().length() == 0);
    }

	
}
