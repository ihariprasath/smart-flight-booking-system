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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingClient bookingClient;

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {

        BookingResponse booking =
                bookingClient.getBooking(request.getBookingId());

        if (booking == null) {
            throw new RuntimeException(
                    "Booking not found: " + request.getBookingId());
        }

        if (!"PENDING".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException(
                    "Booking is not in PENDING state. Current status: "
                            + booking.getStatus());
        }

        List<Payment> existingPayments =
                paymentRepository.findAllByBookingId(request.getBookingId());

        if (!existingPayments.isEmpty()) {
            log.warn("Payment already exists for booking {}",
                    request.getBookingId());

            return mapToResponse(existingPayments.get(0));
        }

        BigDecimal amount = booking.getTotalAmount();

        boolean paymentSuccess =
                simulatePayment(request.getPaymentMethod());

        Payment payment = Payment.builder()
                .bookingId(request.getBookingId())
                .amount(amount)
                .paymentMethod(request.getPaymentMethod())
                .transactionRef(UUID.randomUUID().toString())
                .status(paymentSuccess
                        ? PaymentStatus.SUCCESS
                        : PaymentStatus.FAILED)
                .createdAt(LocalDateTime.now())
                .build();

        payment = paymentRepository.save(payment);

        try {

            if (paymentSuccess) {
                bookingClient.confirmBooking(request.getBookingId());
            } else {
                bookingClient.failBooking(request.getBookingId());
            }

        } catch (Exception ex) {
            log.error("Booking update failed after payment", ex);
        }

        return mapToResponse(payment);
    }

    private boolean simulatePayment(String method) {
        // simple mock gateway (80% success)
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