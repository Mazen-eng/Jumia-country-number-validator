package com.jumia.CountryNumberValidator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Mazen
 * Model represents country's data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Country {

	private String countryName;
	private String countryCode;
	private String pattern;
	
}
