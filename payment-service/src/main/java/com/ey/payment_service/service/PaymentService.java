package com.ey.payment_service.service;

import com.ey.payment_service.entity.*;
import com.ey.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repo;

    public Payment pay(Long bookingId, Double amount) {

        Payment payment = Payment.builder()
                .bookingId(bookingId)
                .amount(amount)
                .status(Status.SUCCESS)
                .createAt(LocalDateTime.now())
                .build();

        return repo.save(payment);
    }

    public Payment refund(Long bookingId) {

        Payment p = repo.findByBookingId(bookingId).orElseThrow();

        p.setStatus(Status.REFUNDED);

        return repo.save(p);
    }

    public Payment get(Long bookingId) {
        return repo.findByBookingId(bookingId).orElseThrow();
    }
}