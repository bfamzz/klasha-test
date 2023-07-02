package com.klasha.test.exception.types;

import com.klasha.test.exception.ApiRequestException;

public class InvalidInputException extends ApiRequestException {
    public InvalidInputException(String message) {
        super(message);
    }
}
