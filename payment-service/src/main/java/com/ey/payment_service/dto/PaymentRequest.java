package com.ey.payment_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    @NotNull
    private Long bookingId;

    @NotNull
    private String paymentMethod;
}
