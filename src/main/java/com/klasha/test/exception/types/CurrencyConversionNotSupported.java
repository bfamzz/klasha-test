package com.klasha.test.exception.types;

import com.klasha.test.exception.ApiRequestException;

public class CurrencyConversionNotSupported extends ApiRequestException {
    public CurrencyConversionNotSupported(String message) {
        super(message);
    }
}
