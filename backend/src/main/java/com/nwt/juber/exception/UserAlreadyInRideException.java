package com.nwt.juber.exception;

public class UserAlreadyInRideException extends RuntimeException {
	public UserAlreadyInRideException() {
	}

	public UserAlreadyInRideException(String message) {
		super(message);
	}

	public UserAlreadyInRideException(String message, Throwable cause) {
		super(message, cause);
	}
}