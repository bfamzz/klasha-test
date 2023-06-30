package com.klasha.test.exception.types;

import com.klasha.test.exception.ApiRequestException;

public class ResourceNotFoundException extends ApiRequestException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
