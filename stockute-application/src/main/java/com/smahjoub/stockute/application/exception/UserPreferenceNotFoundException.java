package com.smahjoub.stockute.application.exception;

/**
 * Thrown when a requested user preference is not found for a given user and key.
 */
public class UserPreferenceNotFoundException extends RuntimeException {
    public UserPreferenceNotFoundException(String message) {
        super(message);
    }
}