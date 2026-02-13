package com.ey.pricing_service.exception;

public class JourneyNotFoundException extends PricingException{
    public JourneyNotFoundException(String message){
        super(message);
    }
}
