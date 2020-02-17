package com.djedra.exception;

public class ParserException extends RuntimeException {

	private static final long serialVersionUID = -8900308184765920490L;

	public ParserException(String message, Throwable t) {
		super(message, t);
	}
}
