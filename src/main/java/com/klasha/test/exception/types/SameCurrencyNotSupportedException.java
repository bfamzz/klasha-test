package com.klasha.test.exception.types;

import com.klasha.test.exception.ApiRequestException;

public class SameCurrencyNotSupportedException extends ApiRequestException {
    public SameCurrencyNotSupportedException(String message) {
        super(message);
    }
}
