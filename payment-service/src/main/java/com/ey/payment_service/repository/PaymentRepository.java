package com.ey.payment_service.repository;

import com.ey.payment_service.entity.Payment;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionRef(String transactionRef);

    Optional<Payment> findByBookingId(Long bookingId);

    List<Payment> findAllByBookingId(@NotNull Long bookingId);

    Optional<Payment> findTopByBookingIdOrderByCreatedAtDesc(@NotNull Long bookingId);
}
