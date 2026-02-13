package com.ey.journey_service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
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

    private Integer totalSeats;
    private Integer businessSeats;
    private Integer economySeats;

    private Integer availableSeats;
    private Integer availableBusinessSeats;
    private Integer availableEconomySeats;

    private String status;
    private BigDecimal baseFare;
}
