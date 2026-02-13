package com.ey.pricing_service.repository;

import com.ey.pricing_service.entity.GstLedger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GstLedgerRepository extends JpaRepository<GstLedger, Long> {
}
