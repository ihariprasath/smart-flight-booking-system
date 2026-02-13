package com.ey.pricing_service.repository;

import com.ey.pricing_service.entity.PricingAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricingAuditRepository extends JpaRepository<PricingAudit, Long> {
}
