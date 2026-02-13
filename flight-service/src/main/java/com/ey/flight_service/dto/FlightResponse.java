package com.ey.flight_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FlightResponse {

    private Long id;
    private String flightNumber;
    private String operator;
    private String aircraftType;
    private Integer totalSeats;
}
