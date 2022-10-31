package com.nwt.juber.exception;

public class MethodNotImplementedException extends RuntimeException {
    public MethodNotImplementedException() {
    }

    public MethodNotImplementedException(String message) {
        super(message);
    }

    public MethodNotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }
}
