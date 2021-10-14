package com.jumia.CountryNumberValidator.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.jumia.CountryNumberValidator.dto.CustomerDto;
import com.jumia.CountryNumberValidator.exception.NoCustomersFoundException;
import com.jumia.CountryNumberValidator.model.Customer;
import com.jumia.CountryNumberValidator.repo.CustomerRepo;
import com.jumia.CountryNumberValidator.utils.Cache;
import com.jumia.CountryNumberValidator.utils.Constants;
import com.jumia.CountryNumberValidator.utils.mapper.MapFromCustomerToDto;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	@Autowired
	private CustomerRepo customerRepo;

	@Autowired
	private MapFromCustomerToDto customerDtoMapper;
	
	@Autowired
	private Cache cache;

	/**
	 * Retrieve the customers based on the filtration criteria and pagination info
	 * @param page :page number
	 * @param size :page size
	 * @param countryCategory :country filter
	 * @param state :state filter
	 * @return response map
	 */
	public Page<CustomerDto> getValidatedCustomersNumbers(int page, int size, String countryCategory, String state){

			Pageable paging = PageRequest.of(page, size);

			if (!countryCategory.equalsIgnoreCase(Constants.ALL_CATEGORIES)
					&& !state.equalsIgnoreCase(Constants.ALL_CATEGORIES)) {
				logger.info("Retrieving customers based on country: "+ countryCategory + " and state: "+ state);
				return filterByCountryAndState(page, size, countryCategory, state);
			} else if (!countryCategory.equalsIgnoreCase(Constants.ALL_CATEGORIES)) {
				logger.info("Retrieving customers based on country: "+ countryCategory);
				return findCustomersByCountry(countryCategory, paging);
			} else if (!state.equalsIgnoreCase(Constants.ALL_CATEGORIES)) {
				logger.info("Retrieving customers based on state: "+ state);
				return filterByState(page, size, state);
			} else {
				logger.info("Retrieving all customers ...");
				return getAllCustomers(paging);
			}
	}

	private Page<CustomerDto> filterByCountryAndState(int page, int size, String countryCategory, String state) {
		Boolean booleanState = mapStateToBoolean(state);
		String countryCode = cache.getCodeFromCountryName(countryCategory);
		List<Customer> customers = findCustomersByCountry(countryCode);
		List<CustomerDto> customersDtoTempList = createCustomerDtoList(customers);
		List<CustomerDto> filteredCustomersDtoList;

		filteredCustomersDtoList = customersDtoTempList.stream()
				.filter(customerDto -> booleanState != null && customerDto.isValid() == booleanState)
				.collect(Collectors.toList());

		if(filteredCustomersDtoList.isEmpty()) {
			logger.error("No customers found that match your criteria!");
			throw new NoCustomersFoundException("No customers found!");
		}
		return createCustomerDtoPage(page, size, filteredCustomersDtoList);
	}

	private Page<CustomerDto> filterByState(int page, int size, String state) {

		Boolean booleanState = mapStateToBoolean(state);
		List<Customer> customers = getAllCustomers();
		List<CustomerDto> customersDtoTempList = createCustomerDtoList(customers);
		List<CustomerDto> filteredCustomersDtoList;

		filteredCustomersDtoList = customersDtoTempList.stream()
				.filter(customerDto -> booleanState != null && customerDto.isValid() == booleanState)
				.collect(Collectors.toList());

		if(filteredCustomersDtoList.isEmpty()) {
			logger.error("No customers found that match your criteria!");
			throw new RuntimeException("No customers found!");
		}
		return createCustomerDtoPage(page, size, filteredCustomersDtoList);
	}

	//Get all customers as a page of customers
	private Page<CustomerDto> getAllCustomers(Pageable paging) {
		Page<Customer> customerPages = customerRepo.findAll(paging);
		
		if(customerPages.getTotalElements() < 1) {
			logger.error("Failed to retrieve all customers page!");
			throw new NoCustomersFoundException("No customers found!");
		}
		
		logger.info("Customers' page retrieved successfully!");
		return createCustomerDtoPage(customerPages);
	}

	//Retrieve a list of all customers
	private List<Customer> getAllCustomers() {
		List<Customer> customers = customerRepo.findAll();
		
		if(customers.isEmpty()) {
			logger.error("Failed to retrieve all customers!");
			throw new NoCustomersFoundException("No customers found!");
		}
		
		logger.info("Customers retrieved successfully!");
		return customers;
	}

	//Converts list of customers to a list of customersDto
	private List<CustomerDto> createCustomerDtoList(List<Customer> customers) {
		return customers.stream()
				.map(customer -> customerDtoMapper.mapToDto(customer))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	//Converts Page of customers to a page of customersDto
	private Page<CustomerDto> createCustomerDtoPage(Page<Customer> customers) {
		return customers.map(customerDtoMapper::mapToDto);
	}

	/**
	 * Find customers by country (Represented as countryCode) and return customer's pages if matches the given country code includes pagination info
	 * @param countryCode
	 * @param paging
	 * @return Customer page
	 * @throws NoCustomersFoundException
	 */
	public Page<CustomerDto> findCustomersByCountry(String countryCategory, Pageable paging) {
		String countryCode = cache.getCodeFromCountryName(countryCategory);

		Page<Customer> customerPagesFilteredByCountry = customerRepo.findByPhoneStartsWith(countryCode, paging);
		if(customerPagesFilteredByCountry == null || customerPagesFilteredByCountry.getTotalElements() == 0) {
			logger.error("No customers found with the specified country code: " + countryCode);
			throw new NoCustomersFoundException("No customers found!");
		}
		return createCustomerDtoPage(customerPagesFilteredByCountry);
	}

	/**
	 * Find customers by country (Represented as countryCode) and return a list of found customers if matches the given country code
	 * @param countryCode
	 * @return List of customers
	 * @throws NoCustomersFoundException
	 */
	public List<Customer> findCustomersByCountry(String countryCode) {
		List<Customer> customerPagesFilteredByCountry = customerRepo.findByPhoneStartsWith(countryCode);
		
		if(customerPagesFilteredByCountry.size() < 1 || customerPagesFilteredByCountry == null) {
			logger.error("No customers found with the specified country code: " + countryCode);
			throw new NoCustomersFoundException("No customers found!");
		}
		return customerPagesFilteredByCountry;
	}
	
	//Map phone number state to boolean
	private Boolean mapStateToBoolean(String state) {
		if (state.equalsIgnoreCase(Constants.STATE_VALID)) {
			return true;
		} else if(state.equalsIgnoreCase(Constants.STATE_INVALID)) {
			return false;
		}
		return null;
	}
	
	//create a customerDto page from pagination info and List of customersDto
	private Page<CustomerDto> createCustomerDtoPage(int pageNumber, int pageSize, List<CustomerDto> filteredNumbersDtoList) {
		
		int startItem = pageNumber * pageSize;
		List<CustomerDto> numbersDtoList;
		
		if (filteredNumbersDtoList.size() < startItem) {
			numbersDtoList = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, filteredNumbersDtoList.size());
			numbersDtoList = filteredNumbersDtoList.subList(startItem, toIndex);
		}
		return new PageImpl<>(numbersDtoList, PageRequest.of(pageNumber, pageSize),
				filteredNumbersDtoList.size());
	}
}
