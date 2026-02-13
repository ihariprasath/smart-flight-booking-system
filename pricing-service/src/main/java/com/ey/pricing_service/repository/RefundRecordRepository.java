package com.ey.pricing_service.repository;

import com.ey.pricing_service.entity.RefundRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRecordRepository extends JpaRepository<RefundRecord, Long> {
}
