package com.nwt.juber.exception;

public class ProfileChangeRequestNotFoundException extends RuntimeException {
    public ProfileChangeRequestNotFoundException() {
    }

    public ProfileChangeRequestNotFoundException(String message) {
        super(message);
    }

    public ProfileChangeRequestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
