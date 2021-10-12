package com.jumia.CountryNumberValidator.exception;

@SuppressWarnings("serial")
public class InvalidCountryNameException extends IllegalArgumentException {
	
	public InvalidCountryNameException(String message) {
		super(message);
	}
	
	public InvalidCountryNameException(String message, Exception exception) {
		super(message, exception);
	}
}
