package com.ey.pricing_service.controller;

import com.ey.pricing_service.dto.PriceRequest;
import com.ey.pricing_service.dto.PriceResponse;
import com.ey.pricing_service.dto.RefundRequest;
import com.ey.pricing_service.dto.RefundResponse;
import com.ey.pricing_service.entity.PricingLedger;
import com.ey.pricing_service.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pricing")
@RequiredArgsConstructor
public class PricingController {

    private final PricingService pricingService;

    @PostMapping("/calculate")
    public PriceResponse calculate(@RequestBody PriceRequest request) {
        return pricingService.calculatePrice(request);
    }

    @PostMapping("/refund/{bookingId}")
    public RefundResponse refund(@PathVariable Long bookingId) {
        return pricingService.calculateRefund(bookingId);
    }
}