package com.jumia.CountryNumberValidator.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jumia.CountryNumberValidator.exception.NoCustomersFoundException;
import com.jumia.CountryNumberValidator.service.CustomerService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping("customers")
public class CustomerController {

	private Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	CustomerService customerService;
	
	/**
	 * 
	 * @param page :page number (For pagination functionality)
	 * @param size :page size (For pagination functionality)
	 * @param countryCategory :country to filter results on
	 * @param state :phone number state to filter results on
	 * @return a response map that contains the required customers data
	 * @throws NoCustomersFoundException
	 */
	@ApiOperation(value = "Find customers by country or phone number state (including pagination info) and return a response map that contains the list of customers if a match is found")
	@GetMapping("find")
	public ResponseEntity<Map<String, Object>> getCategorizedCustomersNumbers(
			@RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = "All") String countryCategory,
			@RequestParam(defaultValue = "All") String state
	        ) throws NoCustomersFoundException{
		
		Map<String, Object> categorizedNumbers = customerService.getValidatedCustomersNumbers(page, size, countryCategory, state);
		
		if(categorizedNumbers.size() > 0) {
			return new ResponseEntity<Map<String, Object>>(categorizedNumbers, HttpStatus.OK);
		}
		
		logger.error("Customers not found!");
		return new ResponseEntity<Map<String, Object>>(HttpStatus.NOT_FOUND);
	}
	
	
}
