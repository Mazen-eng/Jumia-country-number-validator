package com.jumia.CountryNumberValidator.service;

import java.util.Map;

import com.jumia.CountryNumberValidator.exception.NoCustomersFoundException;

/**
 * 
 * @author Mazen
 * Abstraction of the customer service
 */
public interface CustomerService {
	
	Map<String, Object> getValidatedCustomersNumbers(int page, int size,String countryCategory, String state) throws NoCustomersFoundException;
}
