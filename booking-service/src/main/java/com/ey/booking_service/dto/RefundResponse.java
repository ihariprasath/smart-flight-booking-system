package com.ey.booking_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RefundResponse {

    private BigDecimal refundAmount;
}
