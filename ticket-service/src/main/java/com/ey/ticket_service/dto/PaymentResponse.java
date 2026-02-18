package com.ey.ticket_service.dto;

import lombok.Data;

@Data
public class PaymentResponse {
    private Long id;
    private Long bookingId;
    private String status;
}
