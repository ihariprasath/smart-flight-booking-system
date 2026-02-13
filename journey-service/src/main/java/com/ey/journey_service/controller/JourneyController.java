package com.ey.journey_service.controller;

import com.ey.journey_service.dto.CreateJourneyRequest;
import com.ey.journey_service.dto.JourneyResponse;
import com.ey.journey_service.entity.Journey;
import com.ey.journey_service.entity.JourneySeat;
import com.ey.journey_service.service.JourneyService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/journeys")
@RequiredArgsConstructor
public class JourneyController {

    private final JourneyService service;

    @PostMapping
    public JourneyResponse create(@RequestBody CreateJourneyRequest request) {
        Journey journey = service.create(request);
        return mapToResponse(journey);
    }

    @GetMapping("/{id}")
    public JourneyResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/search")
    public List<JourneyResponse> search(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return service.search(source, destination, date);
    }

    @PutMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id) {
        service.cancelJourney(id);
    }

    @GetMapping("/{id}/base-fare")
    public BigDecimal getBaseFare(@PathVariable Long id) {
        return service.getBaseFare(id);
    }

    @GetMapping("/{journeyId}/seats")
    public List<JourneySeat> getSeats(@PathVariable Long journeyId) {
        return service.getSeats(journeyId);
    }

    @PutMapping ("/{journeyId}/seats/lock")
    public void lockSeats(@PathVariable Long journeyId,
                          @RequestParam Long bookingId,
                          @RequestBody List<String> seatNumbers) {

        service.lockSeats(journeyId,bookingId, seatNumbers);
    }

    @PostMapping("/{journeyId}/confirm")
    public void confirmSeats(@PathVariable Long journeyId,
                             @RequestParam Long bookingId,
                             @RequestBody List<String> seats) {

        service.confirmSeats(journeyId, seats, bookingId);
    }

    @PutMapping("/{journeyId}/seats/release")
    public void releaseSeats(@PathVariable Long journeyId,
                             @RequestParam Long bookingId,
                             @RequestBody List<String> seats) {

        service.releaseSeats(journeyId, seats, bookingId);
    }

    private JourneyResponse mapToResponse(Journey journey) {
        return JourneyResponse.builder()
                .id(journey.getId())
                .flightId(journey.getFlightId())
                .source(journey.getSource())
                .destination(journey.getDestination())
                .departureTime(journey.getDepartureTime())
                .arrivalTime(journey.getArrivalTime())
                .journeyDate(journey.getJourneyDate())

                .totalSeats(journey.getTotalSeats())
                .businessSeats(journey.getBusinessSeats())
                .economySeats(journey.getEconomySeats())

                .availableSeats(journey.getAvailableSeats())
                .availableBusinessSeats(journey.getAvailableBusinessSeats())
                .availableEconomySeats(journey.getAvailableEconomySeats())

                .baseFare(journey.getBaseFare())
                .status(journey.getStatus().name())

                .build();
    }
}