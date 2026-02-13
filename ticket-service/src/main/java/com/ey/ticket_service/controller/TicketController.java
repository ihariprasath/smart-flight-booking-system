package com.ey.ticket_service.controller;

import com.ey.ticket_service.dto.TicketResponse;
import com.ey.ticket_service.entity.Ticket;
import com.ey.ticket_service.service.TicketService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@Builder
@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService service;


    @PostMapping("/create")

    public TicketResponse create(
            @RequestParam Long bookingId,
            @RequestParam String flightNumber) {
        return map(service.create(bookingId, flightNumber));
    }


    @GetMapping("/{bookingId}")
    public TicketResponse get(@PathVariable Long bookingId) {

        return map(service.get(bookingId));
    }


    private TicketResponse map(Ticket t) {
        return TicketResponse.builder()
                .bookingId(t.getBookingId())
                .passengerName(t.getPassengerName())
                .pnr(t.getPnr())
                .flightNumber(t.getFlightNumber())
                .build();
    }
}