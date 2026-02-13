package com.ey.pricing_service.dto;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RefundResponse {

    private Long bookingId;
    private BigDecimal paidAmount;
    private BigDecimal gstDeducted;
    private BigDecimal cancellationFee;
    private BigDecimal refundAmount;
}
