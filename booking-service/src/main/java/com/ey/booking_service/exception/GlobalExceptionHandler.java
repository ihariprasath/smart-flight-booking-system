package com.ey.booking_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<?> handleBooking(BookingException ex){
        return ResponseEntity.badRequest().body(
                Map.of("error",ex.getMessage())
        );
    }
}
