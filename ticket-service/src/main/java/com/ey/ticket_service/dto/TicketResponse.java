package com.ey.ticket_service.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TicketResponse {
    private Long id;
    private String ticketNumber;
    private Long bookingId;
    private Long paymentId;
    private String seatNumbers;
    private String pdfPath;
}
