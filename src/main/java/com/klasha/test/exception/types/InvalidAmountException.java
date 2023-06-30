package com.klasha.test.exception.types;

import com.klasha.test.exception.ApiRequestException;

public class InvalidAmountException extends ApiRequestException {
    public InvalidAmountException(String message) {
        super(message);
    }
}
