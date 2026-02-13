package com.ey.payment_service.dto;

import com.ey.payment_service.entity.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {

    private Long id;
    private Long bookingId;
    private Double amount;
    private Status status;

}
