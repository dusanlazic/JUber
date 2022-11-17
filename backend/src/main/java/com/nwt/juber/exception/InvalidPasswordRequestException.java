package com.nwt.juber.exception;

public class InvalidPasswordRequestException extends RuntimeException {
    public InvalidPasswordRequestException() {
    }

    public InvalidPasswordRequestException(String message) {
        super(message);
    }

    public InvalidPasswordRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
