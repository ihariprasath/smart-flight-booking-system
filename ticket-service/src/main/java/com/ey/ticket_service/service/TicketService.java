package com.ey.ticket_service.service;

import com.ey.ticket_service.client.BookingClient;
import com.ey.ticket_service.client.PaymentClient;
import com.ey.ticket_service.dto.*;
import com.ey.ticket_service.entity.Ticket;
import com.ey.ticket_service.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final BookingClient bookingClient;
    private final PaymentClient paymentClient;
    private final PdfService pdfService;

    @Transactional
    public TicketResponse generateTicket(Long bookingId) {

        ticketRepository.findByBookingId(bookingId)
                .ifPresent(t -> {
                    throw new RuntimeException("Ticket already generated");
                });

        BookingResponse booking = bookingClient.getById(bookingId);

        if (!"CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException("Booking not confirmed");
        }

        PaymentResponse payment = paymentClient.getByBookingId(bookingId);

        if (!"SUCCESS".equalsIgnoreCase(payment.getStatus())) {
            throw new RuntimeException("Payment not successful");
        }

        String ticketNumber = "TKT-" + UUID.randomUUID().toString().substring(0, 8);

        String passengerName = booking.getPassengers().get(0).getName();
        String seats = String.join(",", booking.getSeatNumbers());

        String pdfPath = pdfService.generateTicketPdf(
                ticketNumber,
                bookingId,
                passengerName,
                seats
        );

        Ticket ticket = Ticket.builder()
                .ticketNumber(ticketNumber)
                .bookingId(bookingId)
                .paymentId(payment.getId())
                .passengerName(passengerName)
                .seatNumbers(seats)
                .pdfPath(pdfPath)
                .createdAt(LocalDateTime.now())
                .build();

        ticket = ticketRepository.save(ticket);

        return map(ticket);
    }

    public TicketResponse getById(Long id) {
        return map(ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found")));
    }

    public String getTicketPdf(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"))
                .getPdfPath();
    }

    private TicketResponse map(Ticket t) {
        return TicketResponse.builder()
                .id(t.getId())
                .ticketNumber(t.getTicketNumber())
                .bookingId(t.getBookingId())
                .paymentId(t.getPaymentId())
                .seatNumbers(t.getSeatNumbers())
                .pdfPath(t.getPdfPath())
                .build();
    }
}