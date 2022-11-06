package com.nwt.juber.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidVerificationTokenException extends AuthenticationException {
    public InvalidVerificationTokenException(String message) {
        super(message);
    }

    public InvalidVerificationTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
