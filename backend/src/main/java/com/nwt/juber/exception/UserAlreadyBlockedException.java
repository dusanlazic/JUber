package com.nwt.juber.exception;

public class UserAlreadyBlockedException extends RuntimeException {
    public UserAlreadyBlockedException() {
    }

    public UserAlreadyBlockedException(String message) {
        super(message);
    }

    public UserAlreadyBlockedException(String message, Throwable cause) {
        super(message, cause);
    }
}
