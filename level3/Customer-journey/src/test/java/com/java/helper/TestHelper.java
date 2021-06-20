package com.java.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.dao.Customer;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class TestHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Customer createACustomer() {
        String randomFirstName = createRandomString();
        String randomLastName = createRandomString();
        return new Customer(randomFirstName, randomLastName);
    }

    public static String createRandomString() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public static Long createRandomLong() {
        return new Random().nextLong();
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
