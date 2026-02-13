package com.ey.booking_service.exception;

public class SeatLockException extends RuntimeException{
    public SeatLockException(String message){
        super(message);
    }
}
