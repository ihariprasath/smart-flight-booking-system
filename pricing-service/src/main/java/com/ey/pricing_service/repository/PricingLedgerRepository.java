package com.ey.pricing_service.repository;

import com.ey.pricing_service.entity.LedgerType;
import com.ey.pricing_service.entity.PricingLedger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PricingLedgerRepository extends JpaRepository<PricingLedger, Long> {

    Optional<PricingLedger> findTopByBookingIdAndLedgerTypeOrderByIdDesc(
            Long bookingId,
            LedgerType ledgerType
    );

}
