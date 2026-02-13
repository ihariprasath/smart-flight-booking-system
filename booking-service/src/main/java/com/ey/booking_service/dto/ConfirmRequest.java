package com.ey.booking_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConfirmRequest {
    private java.math.BigDecimal paidAmount;
}
