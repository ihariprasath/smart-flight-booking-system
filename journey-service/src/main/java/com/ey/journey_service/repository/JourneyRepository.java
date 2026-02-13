package com.ey.journey_service.repository;

import com.ey.journey_service.entity.Journey;
import com.ey.journey_service.entity.JourneyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface JourneyRepository extends JpaRepository<Journey, Long> {

    boolean existsByFlightIdAndJourneyDateAndDepartureTime(Long flightId, LocalDate journeyDate, LocalTime departureTime);

    List<Journey> findBySourceAndDestinationAndJourneyDateAndStatus(
            String Source,
            String destination,
            LocalDate journeyDate,
            JourneyStatus status
    );
}
