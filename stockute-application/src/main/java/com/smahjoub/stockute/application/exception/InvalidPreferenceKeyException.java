package com.smahjoub.stockute.application.exception;

/**
 * Thrown when attempting to save a user preference with an invalid/unauthorized key.
 * The key must be in the allowed keys list defined in application.yml.
 */
public class InvalidPreferenceKeyException extends RuntimeException {
    
    public InvalidPreferenceKeyException(String message) {
        super(message);
    }
}
