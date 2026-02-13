package com.ey.journey_service.exception;

public class JourneyCancelletionNotAllowedException extends RuntimeException {
    public JourneyCancelletionNotAllowedException(String message) {
        super(message);
    }
}
