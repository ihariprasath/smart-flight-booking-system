package com.ey.flight_service.controller;

import com.ey.flight_service.dto.CreateFlightRequest;
import com.ey.flight_service.dto.FlightResponse;
import com.ey.flight_service.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService service;

    @PostMapping
    public FlightResponse create(@RequestBody CreateFlightRequest req) {
        return service.create(req);
    }

    @GetMapping("/{id}")
    public FlightResponse get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/by-number/{number}")
    public FlightResponse getByNumber(@PathVariable String number) {
        return service.getByNumber(number);
    }
}
