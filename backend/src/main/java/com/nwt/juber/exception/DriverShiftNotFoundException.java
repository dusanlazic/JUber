package com.nwt.juber.exception;

public class DriverShiftNotFoundException extends RuntimeException {
    public DriverShiftNotFoundException() {
    }

    public DriverShiftNotFoundException(String message) {
        super(message);
    }

    public DriverShiftNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
