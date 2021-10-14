package com.jumia.CountryNumberValidator.service;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.jumia.CountryNumberValidator.dto.CustomerDto;
import com.jumia.CountryNumberValidator.exception.InvalidCountryNameException;
import com.jumia.CountryNumberValidator.exception.NoCustomersFoundException;

/**
 * 
 * @author Mazen
 * Abstraction of the customer service
 */
public interface CustomerService {

	Page<CustomerDto> getValidatedCustomersNumbers(int page, int size,String countryCategory, String state);
}
