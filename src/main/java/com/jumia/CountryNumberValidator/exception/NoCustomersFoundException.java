package com.jumia.CountryNumberValidator.exception;

@SuppressWarnings("serial")
public class NoCustomersFoundException extends RuntimeException {

	public NoCustomersFoundException(String message) {
		super(message);
	}
}
