package com.jumia.CountryNumberValidator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author Mazen
 * CustomerDTO to encapsulate required data for the user
 */
@Data
@AllArgsConstructor
public class CustomerDto{

	private Long id;
	private String customerName;
	private String phoneNumber;
	private String countryCode;
	private String countryName;
	private boolean isValid;
}
