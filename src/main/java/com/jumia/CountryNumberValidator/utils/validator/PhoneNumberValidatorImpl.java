package com.jumia.CountryNumberValidator.utils.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Mazen
 * The implementation of the validator interface that provides
 * validation for the phone number
 */
@Component
public class PhoneNumberValidatorImpl implements Validator {

	private Logger logger = LoggerFactory.getLogger(PhoneNumberValidatorImpl.class);
	/**
	 * @param reference is the country code pattern
	 * @param the phone number to be validated
	 * @return true if the phone number is valid
	 */
	@Override
	public boolean validate(String reference, String validationTarget) {
		
		logger.info("Validating phone number: " + validationTarget);
		return validationTarget.matches(reference);
	}

}
