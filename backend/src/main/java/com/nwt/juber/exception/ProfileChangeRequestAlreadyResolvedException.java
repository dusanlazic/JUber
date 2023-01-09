package com.nwt.juber.exception;

public class ProfileChangeRequestAlreadyResolvedException extends RuntimeException {
    public ProfileChangeRequestAlreadyResolvedException() {
    }

    public ProfileChangeRequestAlreadyResolvedException(String message) {
        super(message);
    }

    public ProfileChangeRequestAlreadyResolvedException(String message, Throwable cause) {
        super(message, cause);
    }
}
