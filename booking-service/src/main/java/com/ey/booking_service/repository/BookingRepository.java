package com.ey.booking_service.repository;

import com.ey.booking_service.entity.Booking;
import com.ey.booking_service.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByStatusAndLockExpiryTimeBefore(
            BookingStatus status,
            LocalDateTime time
    );
}
