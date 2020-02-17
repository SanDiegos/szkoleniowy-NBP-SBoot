package com.djedra.exception;

public class ConnectionException extends RuntimeException {

	private static final long serialVersionUID = -7691470463170922910L;

	public ConnectionException(String message) {
		super(message);
	}

	public ConnectionException(String message, Throwable t) {
		super(message, t);
	}
}
