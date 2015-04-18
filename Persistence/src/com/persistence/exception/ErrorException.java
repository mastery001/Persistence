package com.persistence.exception;

public class ErrorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3108812025350030158L;

	public ErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ErrorException(String message) {
		super(message);
	}
	
	
}
