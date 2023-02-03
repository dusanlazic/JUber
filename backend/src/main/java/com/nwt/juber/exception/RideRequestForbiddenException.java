package com.nwt.juber.exception;

public class RideRequestForbiddenException extends RuntimeException {
    public RideRequestForbiddenException() {
    }

    public RideRequestForbiddenException(String message) {
        super(message);
    }

    public RideRequestForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
