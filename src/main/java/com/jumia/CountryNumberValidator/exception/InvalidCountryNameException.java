package com.jumia.CountryNumberValidator.exception;

@SuppressWarnings("serial")
public class InvalidCountryNameException extends RuntimeException {
	
	public InvalidCountryNameException(String message) {
		super(message);
	}
}
