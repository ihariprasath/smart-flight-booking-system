package com.ey.search_service.service;

import com.ey.search_service.client.JourneyClient;
import com.ey.search_service.dto.JourneyResponse;
import com.ey.search_service.dto.SearchResponse;
import com.ey.search_service.exception.NoFlightsAvailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final JourneyClient journeyClient;

    public List<SearchResponse> search(String source,
                                       String destination,
                                       String date,
                                       int passengers) {
        List<JourneyResponse> journeys =
                journeyClient.searchJourneys(source, destination, date);

        List<SearchResponse> results = journeys.stream()
                .filter(j -> j.getStatus().equals("SCHEDULED"))
                .filter(j -> j.getAvailableSeats() >= passengers)
                .map(j -> SearchResponse.builder()
                        .id(j.getId())
                        .flightId(j.getFlightId())
                        .source(j.getSource())
                        .destination(j.getDestination())
                        .journeyDate(j.getJourneyDate().toString())
                        .departureTime(j.getDepartureTime().toString())
                        .arrivalTime(j.getArrivalTime().toString())
                        .availableSeats(j.getAvailableSeats())
                        .build())
                .collect(Collectors.toList());
        if(results.isEmpty()){
            throw new NoFlightsAvailableException("No flights available for selected route and date");
        }
        return results;
    }
}
