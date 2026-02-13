package com.ey.flight_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFlightRequest {

    private String flightNumber;
    private String operator;
    private String aircraftType;
    private int totalSeats;
}
