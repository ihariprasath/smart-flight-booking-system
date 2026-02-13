package com.ey.journey_service.exception;

public class JourneyAlreadyCancelledException extends RuntimeException {
    public JourneyAlreadyCancelledException(String message) {
        super(message);
    }
}
