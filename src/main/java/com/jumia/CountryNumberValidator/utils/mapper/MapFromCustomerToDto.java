package com.jumia.CountryNumberValidator.utils.mapper;

import java.util.List;

import com.jumia.CountryNumberValidator.dto.CustomerDto;
import com.jumia.CountryNumberValidator.exception.InvalidCountryNameException;
import com.jumia.CountryNumberValidator.model.Customer;

/**
 * 
 * @author Mazen
 * abstraction for the mapping to DTO
 */
public interface MapFromCustomerToDto {
	CustomerDto mapToDto(Customer customer);
}
