package com.ey.ticket_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingResponse {
    private Long id;
    private String status;
    private List<String> seatNumbers;
    private List<PassengerResponse> passengers;
}
