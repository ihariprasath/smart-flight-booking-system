package com.ey.pricing_service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PriceResponse {

    private Long bookingId;
    private Long journeyId;
    private BigDecimal baseFare;
    private BigDecimal surgeFare;
    private BigDecimal gstAmount;
    private BigDecimal totalAmount;
}
