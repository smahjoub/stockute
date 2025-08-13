package com.smahjoub.stockute.adapters.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
@Getter
public class UnauthorizedAccessException extends Exception {

    private final static String defaultMessage = "Unauthorized to access to this resource";
    private final HttpStatus status = HttpStatus.UNAUTHORIZED;
    private final String resourceType;

    public UnauthorizedAccessException(final String resourceType, final String message) {
        super(message);
        this.resourceType = resourceType;
    }

    public UnauthorizedAccessException(final String resourceType) {
        super(defaultMessage);
        this.resourceType = resourceType;
    }

}
