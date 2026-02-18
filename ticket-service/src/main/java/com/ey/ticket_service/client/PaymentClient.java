package com.ey.ticket_service.client;

import com.ey.ticket_service.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @GetMapping("/payments/booking/{bookingId}")
    PaymentResponse getByBookingId(@PathVariable Long bookingId);
}
