package com.klasha.test.exception.types;

import com.klasha.test.exception.ApiRequestException;

public class InternalServerErrorException extends ApiRequestException {

    public InternalServerErrorException(String message) {
        super(message);
    }
}
