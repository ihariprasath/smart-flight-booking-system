package com.ey.search_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchResponse {

    private Long id;
    private Long flightId;
    private String source;
    private String destination;
    private String journeyDate;
    private String departureTime;
    private String arrivalTime;
    private Integer availableSeats;
}
