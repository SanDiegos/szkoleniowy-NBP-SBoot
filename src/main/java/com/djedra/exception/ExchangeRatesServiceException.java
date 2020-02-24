package com.djedra.exception;

public class ExchangeRatesServiceException extends RuntimeException {

	private static final long serialVersionUID = -7691470463170922910L;

	public ExchangeRatesServiceException(String message) {
		super(message);
	}

	public ExchangeRatesServiceException(String message, Throwable t) {
		super(message, t);
	}
}
