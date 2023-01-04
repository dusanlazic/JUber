package com.nwt.juber.exception;

public class ConversationNotFoundException extends RuntimeException {
    public ConversationNotFoundException() {
    }

    public ConversationNotFoundException(String message) {
        super(message);
    }

    public ConversationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
