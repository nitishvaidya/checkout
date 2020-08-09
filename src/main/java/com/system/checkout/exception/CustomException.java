package com.system.checkout.exception;

public class CustomException extends Exception {

	private String message;

	public CustomException(String message) {
		super(message);
	}

}
