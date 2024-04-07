package com.bootcamptoprod.bulkhead.exception;

public class Error {

    public Error(String message) {
        this.message = message;
    }

    private String message;

    public String getMessage() {
        return message;
    }
}
