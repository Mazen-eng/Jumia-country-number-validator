package com.jumia.CountryNumberValidator.utils.validator;

/**
 * 
 * @author Mazen
 * Validator interface to abstract the validator implementation
 */
public interface Validator {
	
	public boolean validate(String reference, String validationTarget);

}
