package com.ey.journey_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
@Entity
@Data
@NoArgsConstructor
@Builder
@Getter
@Setter
@AllArgsConstructor
@Table(
        name = "journey",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {
                        "flight_id",
                        "journey_date",
                        "departure_time"
                }
        )
)
public class Journey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long flightId;
    private LocalDate journeyDate;

    private String source;
    private String destination;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    @Column(nullable = false)
    private Integer totalSeats;

    private  int businessSeats;
    private int economySeats;
    @Column(nullable = false)
    private Integer availableSeats;
    private int availableBusinessSeats;
    private int availableEconomySeats;

    private BigDecimal baseFare;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JourneyStatus status;

}
