package com.ey.payment_service.dto;

import lombok.Data;

@Data
public class PaymentRequest {

    private Long bookingId;
    private Double amount;
}
