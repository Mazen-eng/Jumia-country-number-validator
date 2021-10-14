package com.jumia.CountryNumberValidator.utils.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jumia.CountryNumberValidator.dto.CustomerDto;
import com.jumia.CountryNumberValidator.exception.InvalidCountryNameException;
import com.jumia.CountryNumberValidator.model.Country;
import com.jumia.CountryNumberValidator.model.Customer;
import com.jumia.CountryNumberValidator.utils.Cache;
import com.jumia.CountryNumberValidator.utils.validator.Validator;

@Component
public class MapFromCustomerToDtoImpl implements MapFromCustomerToDto {

  private static final Logger logger = LoggerFactory.getLogger(MapFromCustomerToDtoImpl.class);

  @Autowired
  private Validator numberValidator;

  @Autowired
  private Cache cache;

  //Mapping to CustomerDto and return a list of CustomersDto
  @Override
  public CustomerDto mapToDto(Customer customer) {

    String countryCode = customer.getPhone().split(" ")[0];
    logger.info("Extracting country code from phone: " + countryCode);

    Country country = cache.getCountryFromCode(countryCode);
    logger.info("Get the country that corresponds to the country code from the cache: " + country.getCountryName());

    if (country == null) {
      return null;
    }

    CustomerDto customerDto = new CustomerDto(customer.getId(), customer.getName(), customer.getPhone(), country.getCountryCode(),
        country.getCountryName(),
        numberValidator.validate(country.getPattern(), customer.getPhone()));
    return customerDto;
  }
}
