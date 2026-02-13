package com.ey.pricing_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PricingAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private Long journeyId;
    private String seatClass;
    private String seatType;

    private BigDecimal baseFare;
    private BigDecimal dynamicFare;
    private BigDecimal gstAmount;
    private BigDecimal totalAmount;

    private LocalDateTime calculatedAt;
}
