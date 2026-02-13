package com.ey.journey_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Data
public class CreateJourneyRequest {

    @NotNull
    private Long flightId;
    @NotNull
    private LocalDate journeyDate;
    @NotBlank
    private String source;
    @NotBlank
    private String destination;
    @NotNull
    private LocalTime departureTime;
    @NotNull
    private LocalTime arrivalTime;
    @NotNull
    private int availableSeats;

    private BigDecimal baseFare;

}
