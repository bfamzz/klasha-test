package com.klasha.test.exception;

import com.klasha.test.exception.types.CurrencyConversionNotSupported;
import com.klasha.test.exception.types.InternalServerErrorException;
import com.klasha.test.exception.types.ResourceNotFoundException;
import com.klasha.test.exception.types.SameCurrencyNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> handleApiRequestException(ResourceNotFoundException exception) {
        ApiException apiException = new ApiException(exception.getMessage(), exception,
                ZonedDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InternalServerErrorException.class)
    public ResponseEntity<Object> handleApiRequestException(InternalServerErrorException exception) {
        ApiException apiException = new ApiException(exception.getMessage(), exception,
                ZonedDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = SameCurrencyNotSupportedException.class)
    public ResponseEntity<Object> handleApiRequestException(SameCurrencyNotSupportedException exception) {
        ApiException apiException = new ApiException(exception.getMessage(), exception,
                ZonedDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CurrencyConversionNotSupported.class)
    public ResponseEntity<Object> handleApiRequestException(CurrencyConversionNotSupported exception) {
        ApiException apiException = new ApiException(exception.getMessage(), exception,
                ZonedDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }
}
