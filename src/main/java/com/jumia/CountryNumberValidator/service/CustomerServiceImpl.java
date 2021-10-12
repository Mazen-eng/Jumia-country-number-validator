package com.jumia.CountryNumberValidator.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jumia.CountryNumberValidator.dto.CustomerDto;
import com.jumia.CountryNumberValidator.exception.NoCustomersFoundException;
import com.jumia.CountryNumberValidator.model.Customer;
import com.jumia.CountryNumberValidator.repo.CustomerRepo;
import com.jumia.CountryNumberValidator.utils.Cache;
import com.jumia.CountryNumberValidator.utils.Constants;
import com.jumia.CountryNumberValidator.utils.mapper.MapFromCustomerToDto;

@Service
public class CustomerServiceImpl implements CustomerService {

	private Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
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
	public Map<String, Object> getValidatedCustomersNumbers(int page, int size, String countryCategory, String state)
			throws NoCustomersFoundException {

			Pageable paging = PageRequest.of(page, size);
			Page<Customer> customerPages;

			if (!countryCategory.equalsIgnoreCase(Constants.ALL_CATEGORIES)
					&& !state.equalsIgnoreCase(Constants.ALL_CATEGORIES)) {
				logger.info("Retrieving customers based on country: "+ countryCategory + " and state: "+ state);
				boolean booleanState = mapStateToBoolean(state);
				String countryCode = cache.getCodeFromCountryName(countryCategory);
				List<Customer> customers = findCustomersByCountry(countryCode);
				List<CustomerDto> customersDtoTempList = createCustomerDtoList(customers);
				List<CustomerDto> filteredCustomersDtoList;

				filteredCustomersDtoList = customersDtoTempList.stream().filter(customerDto -> customerDto.isValid() == booleanState)
						.collect(Collectors.toList());
				
				if(filteredCustomersDtoList.size()<1) {
					logger.error("No customers found that match your criteria!");
					throw new NoCustomersFoundException("No customers found!");
				}

				Page<CustomerDto> customerDTOPage = createCustomerDtoPage(page, size, filteredCustomersDtoList);
				return createResponseObjectFromCustomerDto(customerDTOPage);
			} else if (!countryCategory.equalsIgnoreCase(Constants.ALL_CATEGORIES)) {
				logger.info("Retrieving customers based on country: "+ countryCategory);
				
				String countryCode = cache.getCodeFromCountryName(countryCategory);
				customerPages = findCustomersByCountry(countryCode, paging);
				return createResponseObjectFromCustomer(customerPages);
				
			} else if (!state.equalsIgnoreCase(Constants.ALL_CATEGORIES)) {
				logger.info("Retrieving customers based on state: "+ state);
				
				boolean booleanState = mapStateToBoolean(state);
				List<Customer> customers = getAllCustomers();
				List<CustomerDto> customersDtoTempList = createCustomerDtoList(customers);
				List<CustomerDto> filteredCustomersDtoList;

				filteredCustomersDtoList = customersDtoTempList.stream().filter(cutomerDto -> cutomerDto.isValid() == booleanState)
						.collect(Collectors.toList());
				
				if(filteredCustomersDtoList.size()<1) {
					
					logger.error("No customers found that match your criteria!");
					throw new IllegalArgumentException("No customers found!");
				}
				
				Page<CustomerDto> customerDTOPage = createCustomerDtoPage(page, size, filteredCustomersDtoList);
				return createResponseObjectFromCustomerDto(customerDTOPage);

			} else {
				logger.info("Retrieving all customers ...");
				customerPages = getAllCustomers(paging);
				return createResponseObjectFromCustomer(customerPages);
			}
	}

	//Get all customers as a page of customers
	private Page<Customer> getAllCustomers(Pageable paging) throws NoCustomersFoundException {
		Page<Customer> customerPages = customerRepo.findAll(paging);
		
		if(customerPages.getTotalElements() < 1) {
			logger.error("Failed to retrieve all customers page!");
			throw new NoCustomersFoundException("No customers found!");
		}
		
		logger.info("Customers' page retrieved successfully!");
		return customerPages;
	}

	//Retrieve a list of all customers
	private List<Customer> getAllCustomers() throws NoCustomersFoundException {
		List<Customer> customers = customerRepo.findAll();
		
		if(customers.size() < 1) {
			logger.error("Failed to retrieve all customers!");
			throw new NoCustomersFoundException("No customers found!");
		}
		
		logger.info("Customers retrieved successfully!");
		return customers;
	}

	//Converts list of customers to a list of customersDto
	private List<CustomerDto> createCustomerDtoList(List<Customer> customers) {
		
		List<CustomerDto> customerDto = new ArrayList<CustomerDto>();
		customerDto = customerDtoMapper.mapToDto(customers);
		
		return customerDto;
	}

	//Maps the customerDtoPage to a response map
	private Map<String, Object> createResponseObjectFromCustomerDto(Page<CustomerDto> customersDtoPage) {
		Map<String, Object> response = new HashMap<>();
		List<CustomerDto> customersDto = new ArrayList<CustomerDto>();

		customersDto = customersDtoPage.getContent();

		logger.info("Creating response map from customerDto page ...");
		response.put("numbers", customersDto);
		response.put("currentPage", customersDtoPage.getNumber());
		response.put("totalItems", customersDtoPage.getTotalElements());
		response.put("totalPages", customersDtoPage.getTotalPages());
		logger.info("Response created  from customerDto page successfully!");

		return response;
	}

	//Maps the customerPage to a response map
	private Map<String, Object> createResponseObjectFromCustomer(Page<Customer> customerPage) {
		
		Map<String, Object> response = new HashMap<>();
		List<Customer> customers = new ArrayList<Customer>();

		customers = customerPage.getContent();
		logger.info("Creating customerDto ...");
		List<CustomerDto> categorizedNumbers = createCustomerDtoList(customers);

		logger.info("Creating response map from customer page ...");
		response.put(Constants.RESPONSE_NUMBERS, categorizedNumbers);
		response.put(Constants.RESPONSE_CURRENT_PAGE, customerPage.getNumber());
		response.put(Constants.RESPONSE_TOTAL_ITEMS, customerPage.getTotalElements());
		response.put(Constants.RESPONSE_TOTAL_PAGES, customerPage.getTotalPages());
		logger.info("Response created  from customer page successfully!");

		return response;
	}

	/**
	 * Find customers by country (Represented as countryCode) and return customer's pages if matches the given country code includes pagination info
	 * @param countryCode
	 * @param paging
	 * @return Customer page
	 * @throws NoCustomersFoundException
	 */
	public Page<Customer> findCustomersByCountry(String countryCode, Pageable paging) throws NoCustomersFoundException {
		Page<Customer> customerPagesFilteredByCountry = customerRepo.findByPhoneStartsWith(countryCode, paging);
		
		if(customerPagesFilteredByCountry.getTotalElements() < 1 || customerPagesFilteredByCountry == null) {
			logger.error("No customers found with the specified country code: " + countryCode);
			throw new NoCustomersFoundException("No customers found!");
		}
		
		return customerPagesFilteredByCountry;
	}

	/**
	 * Find customers by country (Represented as countryCode) and return a list of found customers if matches the given country code
	 * @param countryCode
	 * @return List of customers
	 * @throws NoCustomersFoundException
	 */
	public List<Customer> findCustomersByCountry(String countryCode) throws NoCustomersFoundException {
		List<Customer> customerPagesFilteredByCountry = customerRepo.findByPhoneStartsWith(countryCode);
		
		if(customerPagesFilteredByCountry.size() < 1 || customerPagesFilteredByCountry == null) {
			logger.error("No customers found with the specified country code: " + countryCode);
			throw new NoCustomersFoundException("No customers found!");
		}

		return customerPagesFilteredByCountry;
	}
	
	//Map phone number state to boolean
	private boolean mapStateToBoolean(String state) {
		if (state.equalsIgnoreCase(Constants.STATE_VALID)) {
			return true;
		} else {
			return false;
		}
	}
	
	//create a customerDto page from pagination info and List of customersDto
	private Page<CustomerDto> createCustomerDtoPage(int pageNumber, int pageSize, List<CustomerDto> filteredNumbersDtoList){
		
		int startItem = pageNumber * pageSize;
		List<CustomerDto> numbersDtoList;
		
		if (filteredNumbersDtoList.size() < startItem) {
			numbersDtoList = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, filteredNumbersDtoList.size());
			numbersDtoList = filteredNumbersDtoList.subList(startItem, toIndex);
		}
		return new PageImpl<CustomerDto>(numbersDtoList, PageRequest.of(pageNumber, pageSize),
				filteredNumbersDtoList.size());
	}
}
