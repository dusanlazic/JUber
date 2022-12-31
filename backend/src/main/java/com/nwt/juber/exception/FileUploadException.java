package com.nwt.juber.exception;

public class FileUploadException extends RuntimeException {
    public FileUploadException() {
    }

    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
