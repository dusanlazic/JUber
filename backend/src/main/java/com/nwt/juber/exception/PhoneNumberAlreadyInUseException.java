package com.nwt.juber.exception;

public class PhoneNumberAlreadyInUseException extends RuntimeException {
    public PhoneNumberAlreadyInUseException() {
    }

    public PhoneNumberAlreadyInUseException(String message) {
        super(message);
    }

    public PhoneNumberAlreadyInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
