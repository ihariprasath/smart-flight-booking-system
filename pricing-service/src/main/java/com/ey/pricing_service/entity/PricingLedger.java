package com.ey.pricing_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PricingLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long journeyId;
    private Long bookingId;

    @Enumerated(EnumType.STRING)
    private SeatClass seatClass;

    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    private BigDecimal baseFare;
    private BigDecimal finalFare;
    private BigDecimal gstAmount;
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private LedgerType ledgerType;

    private LocalDateTime createdAt;
}
