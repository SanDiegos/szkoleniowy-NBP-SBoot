package com.djedra.exception;

public class CurrencyServiceException extends RuntimeException {

	private static final long serialVersionUID = -1680393975558369731L;

	public CurrencyServiceException(String message) {
		super(message);
	}

	public CurrencyServiceException(String message, Throwable t) {
		super(message, t);
	}
}
