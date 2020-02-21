package com.djedra.exception;

public class ExchangeRatesTableServiceException extends RuntimeException {

	private static final long serialVersionUID = -7691470463170922910L;

	public ExchangeRatesTableServiceException(String message) {
		super(message);
	}

	public ExchangeRatesTableServiceException(String message, Throwable t) {
		super(message, t);
	}
}
