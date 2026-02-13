package com.ey.booking_service.client;

import com.ey.booking_service.dto.PriceRequest;
import com.ey.booking_service.dto.PriceResponse;
import com.ey.booking_service.dto.RefundRequest;
import com.ey.booking_service.dto.RefundResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "pricing-service")
public interface PricingClient {

    @PostMapping("/pricing/calculate")
    PriceResponse calculate(@RequestBody PriceRequest request);

    @PostMapping("/pricing/refund/{bookingId}")
    RefundResponse calculateRefund(@RequestBody RefundRequest request);
}
