package com.ey.journey_service.exception;

public class JourneyAlreadyExistsException extends RuntimeException{
    public JourneyAlreadyExistsException(String message){
        super(message);
    }
}
