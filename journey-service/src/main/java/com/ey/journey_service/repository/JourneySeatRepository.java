package com.ey.journey_service.repository;

import com.ey.journey_service.entity.JourneySeat;
import com.ey.journey_service.entity.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JourneySeatRepository extends JpaRepository<JourneySeat, Long> {

    List<JourneySeat> findByJourneyId(Long journeyId);

    List<JourneySeat> findByJourneyIdAndSeatNumberIn(Long journeyId, List<String> seatNumbers);

    List<JourneySeat> findByStatusAndLockedUntilBefore(SeatStatus status, LocalDateTime time);

    Optional <JourneySeat> findByJourneyIdAndSeatNumber(Long journeyId, String seatNumber);
}
