package com.nwt.juber.exception;

public class EndRideException extends RuntimeException{
    public EndRideException() {
    }
    public EndRideException(String message) {
        super(message);
    }
    public EndRideException(String message, Throwable cause) {
        super(message, cause);
    }
}
