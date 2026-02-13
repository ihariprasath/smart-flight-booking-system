package com.ey.ticket_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketResponse {

    private Long bookingId;
    private String passengerName;
    private String pnr;
    private String flightNumber;
}
