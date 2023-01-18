package com.nwt.juber.exception;

public class StartRideException extends RuntimeException {
    public StartRideException() {
    }
    public StartRideException(String message) {
        super(message);
    }
    public StartRideException(String message, Throwable cause) {
        super(message, cause);
    }
}
