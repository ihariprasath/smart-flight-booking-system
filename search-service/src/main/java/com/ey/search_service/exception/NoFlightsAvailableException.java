package com.ey.search_service.exception;

public class NoFlightsAvailableException extends RuntimeException{
    public NoFlightsAvailableException(String message){
        super(message);
    }
}
