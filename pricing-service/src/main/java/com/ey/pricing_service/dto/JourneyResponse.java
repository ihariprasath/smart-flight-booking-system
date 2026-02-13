package com.ey.pricing_service.dto;

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
    private int businessSeats;
    private int economySeats;

    private Integer availableSeats;
    private int availableBusinessSeats;
    private int availableEconomySeats;

    private String status;
    private BigDecimal baseFare;
}
