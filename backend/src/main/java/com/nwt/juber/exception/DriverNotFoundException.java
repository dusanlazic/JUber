package com.nwt.juber.exception;

public class DriverNotFoundException extends Exception {
	public DriverNotFoundException() {
		super("Driver not found");
	}

	public DriverNotFoundException(String message) {
		super(message);
	}
}
