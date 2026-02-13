package com.ey.payment_service.controller;

import com.ey.payment_service.dto.PaymentRequest;
import com.ey.payment_service.dto.PaymentResponse;
import com.ey.payment_service.entity.Payment;
import com.ey.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    public PaymentResponse pay(@RequestBody PaymentRequest request) {

        Payment p = service.pay(
                request.getBookingId(),
                request.getAmount()
        );

        return map(p);
    }

    @GetMapping("/{bookingId}")
    public PaymentResponse get(@PathVariable Long bookingId) {

        return map(service.get(bookingId));
    }

    @PutMapping("/refund/{bookingId}")
    public PaymentResponse refund(@PathVariable Long bookingId) {

        return map(service.refund(bookingId));
    }

    private PaymentResponse map(Payment p) {
        return PaymentResponse.builder()
                .id(p.getId())
                .bookingId(p.getBookingId())
                .amount(p.getAmount())
                .status(p.getStatus())
                .build();
    }
}