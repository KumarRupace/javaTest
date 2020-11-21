package com.java.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.dao.Customer;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class TestUtilsFactory {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Customer aRandomCustomer() {
        String randomFirstName = aRandomString();
        String randomLastName = aRandomString();
        return new Customer(randomFirstName, randomLastName);
    }

    public static String aRandomString() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public static Long aRandomLong() {
        return new Random().nextLong();
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
