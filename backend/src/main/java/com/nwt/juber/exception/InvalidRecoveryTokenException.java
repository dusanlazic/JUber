package com.nwt.juber.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidRecoveryTokenException extends AuthenticationException {
    public InvalidRecoveryTokenException(String message) {
        super(message);
    }

    public InvalidRecoveryTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
