package com.ey.flight_service.service;

import com.ey.flight_service.Exception.FlightNotFoundException;
import com.ey.flight_service.dto.CreateFlightRequest;
import com.ey.flight_service.dto.FlightResponse;
import com.ey.flight_service.entity.Flight;
import com.ey.flight_service.repository.FlightRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Builder
public class FlightService {

    private final FlightRepository repo;

    public FlightResponse create(CreateFlightRequest req) {
        Flight flight = Flight.builder()
                .flightNumber(req.getFlightNumber())
                .operator(req.getOperator())
                .aircraftType(req.getAircraftType())
                .totalSeats(req.getTotalSeats())
                .build();

        Flight saved = repo.save(flight);

        return toResponse(saved);
    }

    public FlightResponse getById(Long id) {
        return repo.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with id: "+id));
    }

    public FlightResponse getByNumber(String number) {
        return repo.findByFlightNumber(number)
                .map(this::toResponse)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with number: "+number));
    }

    private FlightResponse toResponse(Flight f) {
        return FlightResponse.builder()
                .id(f.getId())
                .flightNumber(f.getFlightNumber())
                .operator(f.getOperator())
                .aircraftType(f.getAircraftType())
                .totalSeats(f.getTotalSeats())
                .build();
    }
}