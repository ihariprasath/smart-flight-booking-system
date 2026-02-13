package com.ey.journey_service.dto;

import lombok.Data;

@Data
public class FlightResponse {

    private Long id;
    private String flightNumber;
    private String operator;
    private String aircraftType;
    private Integer totalSeats;
    private int availableSeats;
}
