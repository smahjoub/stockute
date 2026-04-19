package com.smahjoub.stockute.application.exception;

public class SecurityNotFoundException extends RuntimeException {
    public SecurityNotFoundException(String message) {
        super(message);
    }
}
