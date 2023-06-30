package com.klasha.test.exception;

import java.time.ZonedDateTime;

public class ApiException {
    private final String message;

    private final Throwable cause;

    private final ZonedDateTime occurredAt;

    public ApiException(String message, Throwable cause, ZonedDateTime occurredAt) {
        this.message = message;
        this.cause = cause;
        this.occurredAt = occurredAt;
    }

    public String getMessage() {
        return message;
    }


    @Override
    public String toString() {
        return "ApiException{" +
                "message='" + message + '\'' +
                ", cause=" + cause +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
