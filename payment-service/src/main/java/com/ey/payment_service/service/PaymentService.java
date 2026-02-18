package com.ey.payment_service.service;

import com.ey.payment_service.client.BookingClient;
import com.ey.payment_service.dto.BookingResponse;
import com.ey.payment_service.dto.PaymentRequest;
import com.ey.payment_service.dto.PaymentResponse;
import com.ey.payment_service.entity.Payment;
import com.ey.payment_service.entity.PaymentStatus;
import com.ey.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingClient bookingClient;

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {

        // 1. Check existing payment
        Optional<Payment> existingOpt =
                paymentRepository.findTopByBookingIdOrderByCreatedAtDesc(
                        request.getBookingId());

        if (existingOpt.isPresent() &&
                existingOpt.get().getStatus() == PaymentStatus.SUCCESS) {

            return mapToResponse(existingOpt.get());
        }

        // 2. Get booking amount
        BookingResponse booking =
                bookingClient.getBooking(request.getBookingId());

        if (booking == null) {
            throw new RuntimeException("Booking not found");
        }

        BigDecimal amount = booking.getTotalAmount();

        // 3. Decide payment status
        PaymentStatus paymentStatus;

        if (request.getForceStatus() != null) {
            paymentStatus = PaymentStatus.valueOf(request.getForceStatus());
        } else {
            paymentStatus = Math.random() > 0.3
                    ? PaymentStatus.SUCCESS
                    : PaymentStatus.FAILED;
        }

        // 4. Save payment
        Payment payment = Payment.builder()
                .bookingId(request.getBookingId())
                .amount(amount)
                .paymentMethod(request.getPaymentMethod())
                .transactionRef("TXN-" + UUID.randomUUID())
                .status(paymentStatus)
                .createdAt(LocalDateTime.now())
                .build();

        payment = paymentRepository.save(payment);

        // 5. CALL BOOKING SERVICE (CRITICAL)
        try {
            if (paymentStatus == PaymentStatus.SUCCESS) {
                log.info("Confirming booking {}", request.getBookingId());
                bookingClient.confirmBooking(request.getBookingId());
            } else {
                log.info("Failing booking {}", request.getBookingId());
                bookingClient.failBooking(request.getBookingId());
            }
        } catch (Exception ex) {
            log.error("Booking update failed: {}", ex.getMessage());
        }

        return mapToResponse(payment);
    }

    private boolean simulatePayment(String method) {
        return Math.random() > 0.2;
    }

    private PaymentResponse mapToResponse(Payment payment) {

        return PaymentResponse.builder()
                .id(payment.getId())
                .bookingId(payment.getBookingId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .transactionRef(payment.getTransactionRef())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public PaymentResponse getByBookingId(Long bookingId) {

        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for bookingId: "+bookingId));
        return mapToResponse(payment);
    }

    public PaymentResponse getByPaymentId(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: "+paymentId));
        return mapToResponse(payment);
    }
}