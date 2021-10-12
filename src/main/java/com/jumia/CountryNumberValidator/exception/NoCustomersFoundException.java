package com.jumia.CountryNumberValidator.exception;

@SuppressWarnings("serial")
public class NoCustomersFoundException extends Exception {

	public NoCustomersFoundException(String message) {
		super(message);
	}
	
	public NoCustomersFoundException(String message, Exception exception) {
		super(message, exception);
	}
}
