package com.onrmount.uploadfilejava;

import org.springframework.http.HttpStatus;

public class DocumentStorageException extends RuntimeException {

    public HttpStatus status;
    public DocumentStorageException(String message, Throwable cause, HttpStatus status) {
        super(message, cause, false, false);
        this.status = status;
    }
    public DocumentStorageException(String message) {
        super(message);
    }

    public DocumentStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentStorageException(String message, HttpStatus status) {
        super(message, null, false, false);
        this.status = status;
    }

}