package com.ey.ticket_service.service;

import com.ey.ticket_service.client.BookingClient;
import com.ey.ticket_service.entity.Ticket;
import com.ey.ticket_service.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository repo;
    private final BookingClient bookingClient;


    public Ticket create(Long bookingId, String flightNumber) {

        var booking = bookingClient.get(bookingId);

        String pnr = generatePNR();

        Ticket t = Ticket.builder()
                .bookingId(bookingId)
                .passengerName(booking.getPassengerName())
                .flightNumber(flightNumber)
                .pnr(pnr)
                .issuedAt(LocalDateTime.now())
                .build();

        return repo.save(t);
    }


    public Ticket get(Long bookingId) {
        return repo.findByBookingId(bookingId).orElseThrow();
    }


    private String generatePNR() {
        return UUID.randomUUID().toString()
                .substring(0, 6)
                .toUpperCase();
    }

//    public Ticket cancelTicket(Long bookingId){
//        Ticket ticket = repo.findByBookingId(bookingId).orElseThrow(() -> new RuntimeException("Ticket not found"));
//
//        if ("CANCELLED".equals(ticket.getStatus())){
//            throw new RuntimeException(("Ticket already cancelled"));
//        }
//
//        bookingClient.cancelBooking(bookingId);
//
//        ticket.setStatus("CANCELLED");
//        return repo.save(ticket);
//    }
}