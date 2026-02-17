package com.ey.payment_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookingResponse {

    private Long id;
    private BigDecimal totalAmount;
    private String status;
}
