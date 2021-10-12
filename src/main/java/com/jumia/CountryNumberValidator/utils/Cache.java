package com.jumia.CountryNumberValidator.utils;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.jumia.CountryNumberValidator.exception.InvalidCountryNameException;
import com.jumia.CountryNumberValidator.model.Country;

@Component
public class Cache {
	
	private Logger logger = LoggerFactory.getLogger(Cache.class);
	
	private ImmutableList<String> countriesNames;
	private ImmutableMap<String, Country> codeToCountriesMap;
	private ImmutableMap<String, String> countryNameToCountryCodeMap;
	
	//Initialize the cache with the required countries' information
	@PostConstruct
	private void initializeCache() {
		
		logger.info("Initializing countries list ...");
		countriesNames = ImmutableList.<String>builder()
		         .add("Cameroon")
		         .add("Ethiopia")
		         .add("Morocco")
		         .add("Mozambique")
		         .add("Uganda")
		         .build();
		logger.info("Countries list initialized successfully!");

		logger.info("Initializing countries' codes to countries map ...");
		codeToCountriesMap = ImmutableMap.<String, Country>builder()
		         .put("(237)", new Country("Cameroon", "(237)", "\\(237\\)\\ ?[2368]\\d{7,8}$"))
		         .put("(251)", new Country("Ethiopia", "(251)", "\\(251\\)\\ ?[1-59]\\d{8}$"))
		         .put("(212)", new Country("Morocco", "(212)", "\\(212\\)\\ ?[5-9]\\d{8}$"))
		         .put("(258)", new Country("Mozambique", "(258)", "\\(258\\)\\ ?[28]\\d{7,8}$"))
		         .put("(256)", new Country("Uganda", "(256)", "\\(256\\)\\ ?\\d{9}$"))
		         .build();
		logger.info("Countries' codes to countries map initialized successfully!");
		
		logger.info("Initializing countries' names to countries' codes map ...");
		countryNameToCountryCodeMap = ImmutableMap.<String, String>builder()
		         .put("cameroon", "(237)")
		         .put("ethiopia", "(251)")
		         .put("morocco", "(212)")
		         .put("mozambique", "(258)")
		         .put("uganda", "(256)")
		         .build();
		logger.info("Countries' names to countries' codes map initialized successfully!");
		logger.info("Countries' information initialized successfully!");
	}
	
	/**
	 * Get a country from the country code
	 * @param code :country code
	 * @return :The country that maps to the given code
	 */
	public Country getCountryFromCode(String code) {
		
		logger.info("Retrieving country ...");
		if(!codeToCountriesMap.containsKey(code.toLowerCase())) {
			logger.error("Failed to retrieve country from the given code!, Invalid code: " + code);
			throw new IllegalArgumentException("Invalid country code: " + code);
		}
		logger.info("Country found successfully!");
		return this.codeToCountriesMap.get(code);
	}
	
	/**
	 * Get country code based on country name
	 * @param countryName
	 * @return countryCode
	 * @throws InvalidCountryNameException
	 */
	public String getCodeFromCountryName(String countryName) throws InvalidCountryNameException {

		logger.info("Retrieving country code ...");
		if(!countryNameToCountryCodeMap.containsKey(countryName.toLowerCase())) {
			logger.error("Failed to retrieve country code from the given country name!, Invalid country name: " + countryName);
			throw new InvalidCountryNameException("Invalid country name: " + countryName);
		}
		logger.info("Country code found successfully!");
		return this.countryNameToCountryCodeMap.get(countryName.toLowerCase());
	}
	
	public ImmutableList<String> getCountriesNames() {
		
		logger.info("Retrieving countries list finished successfully!");
		return this.countriesNames;
	}
}
