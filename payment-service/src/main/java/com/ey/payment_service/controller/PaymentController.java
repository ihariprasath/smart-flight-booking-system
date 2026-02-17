package com.ey.payment_service.controller;

import com.ey.payment_service.dto.PaymentRequest;
import com.ey.payment_service.dto.PaymentResponse;
import com.ey.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse processPayment(
            @Valid @RequestBody PaymentRequest request) {

        return paymentService.processPayment(request);
    }

    @GetMapping("/booking/{bookingId}")
    public PaymentResponse getByBookingId(
            @PathVariable Long bookingId) {
        return paymentService.getByBookingId(bookingId);
    }

    @GetMapping("/{paymentId}")
    public PaymentResponse getByPaymentId(
            @PathVariable Long paymentId){
        return paymentService.getByPaymentId(paymentId);
    }
}