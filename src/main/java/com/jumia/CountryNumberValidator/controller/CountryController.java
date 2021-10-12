package com.jumia.CountryNumberValidator.controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jumia.CountryNumberValidator.utils.Cache;

import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping("countries")
public class CountryController {
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(CountryController.class);
	@Autowired
	private Cache cache;
	
	/**
	 * gets all countries in exist in the cache, this API is called to initialize 
	 * the list of countries in the front end
	 * 
	 * @return a list of all customers in the cache
	 */
	
	@ApiOperation(value = "Retrieve countries' names from the cache")
	@GetMapping("get")
	public ResponseEntity<List<String>> getCountriesList(){
		List<String> countries = cache.getCountriesNames();
		
		//Return ok and the list of countries if there are countries in the cache
		if(countries.size() > 0) {
			return new ResponseEntity<List<String>>(countries, HttpStatus.OK);
		}
		
		logger.error("Error retrieving countries' list from cache!");
		return new ResponseEntity<List<String>>(HttpStatus.NOT_FOUND);
	}

}
