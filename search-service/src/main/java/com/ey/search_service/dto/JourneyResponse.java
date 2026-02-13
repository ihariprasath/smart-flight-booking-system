package com.ey.search_service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Data
public class JourneyResponse {

    private Long id;
    private Long flightId;

    private LocalDate journeyDate;
    private String source;
    private String destination;

    private LocalTime departureTime;
    private LocalTime arrivalTime;

    private Integer availableSeats;
    private String status;
}
