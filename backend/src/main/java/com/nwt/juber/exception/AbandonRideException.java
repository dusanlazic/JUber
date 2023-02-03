package com.nwt.juber.exception;

public class AbandonRideException extends RuntimeException {
    public AbandonRideException() {
    }

    public AbandonRideException(String message) {
        super(message);
    }

    public AbandonRideException(String message, Throwable cause) {
        super(message, cause);
    }
}