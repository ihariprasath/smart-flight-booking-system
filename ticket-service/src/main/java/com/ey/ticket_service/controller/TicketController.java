package com.ey.ticket_service.controller;

import com.ey.ticket_service.dto.TicketResponse;
import com.ey.ticket_service.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    // 🎟️ generate ticket
    @PostMapping("/generate/{bookingId}")
    public TicketResponse generate(@PathVariable Long bookingId) {
        return ticketService.generateTicket(bookingId);
    }

    // 🔍 get ticket
    @GetMapping("/{id}")
    public TicketResponse get(@PathVariable Long id) {
        return ticketService.getById(id);
    }

    // 📄 get pdf path
    @GetMapping("/{id}/pdf")
    public String getPdf(@PathVariable Long id) {
        return ticketService.getTicketPdf(id);
    }
}