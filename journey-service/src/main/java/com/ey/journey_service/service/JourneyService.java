package com.ey.journey_service.service;

import com.ey.journey_service.client.FlightClient;
import com.ey.journey_service.dto.CreateJourneyRequest;
import com.ey.journey_service.dto.FlightResponse;
import com.ey.journey_service.dto.JourneyResponse;
import com.ey.journey_service.entity.*;
import com.ey.journey_service.repository.JourneyRepository;
import com.ey.journey_service.repository.JourneySeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JourneyService {

    private final FlightClient flightClient;
    private final JourneyRepository journeyRepo;
    private final JourneySeatRepository seatRepo;

    @Transactional
    public Journey create(CreateJourneyRequest request) {

        FlightResponse flight = flightClient.getById(request.getFlightId());

        int totalSeats = flight.getTotalSeats();

        int businessSeats = (int) (totalSeats * 0.2);   // 20% business
        int economySeats = totalSeats - businessSeats;  // 80% economy

        int availableSeats = request.getAvailableSeats();

        if(availableSeats > totalSeats){
            throw new IllegalArgumentException(
                    "Available seats cannot exceeds total seats of the flight"
            );
        }

        if(availableSeats < 0){
            throw new IllegalArgumentException(
                    "Available seats cannot be negative"
            );
        }

        int availableBusinessSeats = (int) (availableSeats * 0.2);
        int availableEconomySeats = availableSeats - availableBusinessSeats;

        Journey journey = new Journey();

        journey.setFlightId(request.getFlightId());
        journey.setSource(request.getSource());
        journey.setDestination(request.getDestination());
        journey.setDepartureTime(request.getDepartureTime());
        journey.setArrivalTime(request.getArrivalTime());
        journey.setJourneyDate(request.getJourneyDate());
        journey.setBaseFare(request.getBaseFare());
        journey.setStatus(JourneyStatus.SCHEDULED);

        journey.setTotalSeats(totalSeats);
        journey.setBusinessSeats(businessSeats);
        journey.setEconomySeats(economySeats);

        journey.setAvailableSeats(availableSeats);
        journey.setAvailableBusinessSeats(availableBusinessSeats);
        journey.setAvailableEconomySeats(availableEconomySeats);

        Journey saved = journeyRepo.save(journey);
        generateSeats(saved,availableSeats);
        return saved;
    }


    private void generateSeats(Journey journey, int totalSeats) {

        List<JourneySeat> seats = new ArrayList<>();

        char[] seatLetters = {'A','B','C','D','E','F'};
        int rows = totalSeats / 6;

        for (int row = 1; row <= rows; row++) {

            for (int col = 0; col < 6; col++) {

                String seatNumber = row + String.valueOf(seatLetters[col]);

                SeatType seatType;

                switch (seatLetters[col]) {
                    case 'A':
                    case 'F':
                        seatType = SeatType.WINDOW;
                        break;

                    case 'B':
                    case 'E':
                        seatType = SeatType.MIDDLE;
                        break;

                    case 'C':
                    case 'D':
                        seatType = SeatType.AISLE;
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value");
                }

                SeatClass seatClass =
                        row <= (int)(rows * 0.2)
                                ? SeatClass.BUSINESS
                                : SeatClass.ECONOMY;

                seats.add(JourneySeat.builder()
                        .journey(journey)
                        .seatNumber(seatNumber)
                        .seatType(seatType)
                        .seatClass(seatClass)
                        .status(SeatStatus.AVAILABLE)
                        .build());
            }
        }

        seatRepo.saveAll(seats);
    }

    @Transactional
    public void lockSeats(Long journeyId,
                          Long bookingId,
                          List<String> seatNumbers) {

        List<JourneySeat> seats =
                seatRepo.findByJourneyIdAndSeatNumberIn(journeyId, seatNumbers);

        if (seats.size() != seatNumbers.size()) {
            throw new RuntimeException("Some seats not found");
        }

        for (JourneySeat seat : seats) {

            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                throw new RuntimeException(
                        "Seat already booked or locked: " + seat.getSeatNumber());
            }

            if (seat.getLockedByBookingId() != null &&
                    !seat.getLockedByBookingId().equals(bookingId) &&
                    seat.getLockedUntil() != null &&
                    seat.getLockedUntil().isAfter(LocalDateTime.now())) {

                throw new RuntimeException(
                        "Seat already locked: " + seat.getSeatNumber());
            }

            seat.setStatus(SeatStatus.LOCKED);
            seat.setLockedByBookingId(bookingId);
            seat.setLockedUntil(LocalDateTime.now().plusMinutes(5));
        }

        seatRepo.saveAll(seats);
    }

    @Transactional
    public void confirmSeats(Long journeyId,
                             List<String> seatNumbers,
                             Long bookingId) {

        List<JourneySeat> seats =
                seatRepo.findByJourneyIdAndSeatNumberIn(journeyId, seatNumbers);

        if (seats.size() != seatNumbers.size()) {
            throw new RuntimeException("Some seats not found");
        }

        int newlyBookedCount = 0;

        for (JourneySeat seat : seats) {

            if (seat.getStatus() == SeatStatus.BOOKED) {
                throw new RuntimeException(
                        "Seat already booked: " + seat.getSeatNumber());
            }

            if (!bookingId.equals(seat.getLockedByBookingId())) {
                throw new RuntimeException(
                        "Seat lock mismatch for seat: " + seat.getSeatNumber());
            }

            seat.setStatus(SeatStatus.BOOKED);
            seat.setLockedUntil(null);
            seat.setLockedByBookingId(null);

            newlyBookedCount++;
        }

        seatRepo.saveAll(seats);


        Journey journey = journeyRepo.findById(journeyId)
                .orElseThrow(() -> new RuntimeException("Journey not found"));

        if (journey.getAvailableSeats() < newlyBookedCount) {
            throw new RuntimeException("Not enough seats available in journey");
        }

        journey.setAvailableSeats(
                journey.getAvailableSeats() - newlyBookedCount
        );

        journeyRepo.save(journey);
    }


    @Transactional
    public void releaseSeats(Long journeyId,
                             List<String> seatNumbers,
                             Long bookingId) {

        List<JourneySeat> seats =
                seatRepo.findByJourneyIdAndSeatNumberIn(journeyId, seatNumbers);

        for (JourneySeat seat : seats) {

            if (bookingId.equals(seat.getLockedByBookingId())) {

                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setLockedByBookingId(null);
                seat.setLockedUntil(null);
            }
        }

        seatRepo.saveAll(seats);

        Journey journey = journeyRepo.findById(journeyId).orElseThrow();
        journey.setAvailableSeats(
                journey.getAvailableSeats() + seatNumbers.size()
        );
        journeyRepo.save(journey);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void releaseExpiredLocks() {

        List<JourneySeat> expired =
                seatRepo.findByStatusAndLockedUntilBefore(
                        SeatStatus.LOCKED,
                        LocalDateTime.now()
                );

        for (JourneySeat seat : expired) {

            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setLockedByBookingId(null);
            seat.setLockedUntil(null);

            Journey journey =
                    journeyRepo.findById(seat.getJourney().getId()).orElseThrow();

            journey.setAvailableSeats(
                    journey.getAvailableSeats() + 1
            );

            journeyRepo.save(journey);
        }

        seatRepo.saveAll(expired);
    }

    private JourneyResponse map(Journey j) {
        return JourneyResponse.builder()
                .id(j.getId())
                .flightId(j.getFlightId())
                .journeyDate(j.getJourneyDate())
                .source(j.getSource())
                .destination(j.getDestination())
                .departureTime(j.getDepartureTime())
                .arrivalTime(j.getArrivalTime())


                .totalSeats(j.getTotalSeats())
                .businessSeats(j.getBusinessSeats())
                .economySeats(j.getEconomySeats())

                .availableSeats(j.getAvailableSeats())
                .availableBusinessSeats(j.getAvailableBusinessSeats())
                .availableEconomySeats(j.getAvailableEconomySeats())

                .status(String.valueOf(j.getStatus()))
                .baseFare(j.getBaseFare())
                .build();
    }

    public JourneyResponse getById(Long id) {

        Journey journey = journeyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Journey not found"));

        return map(journey);
    }


    public List<JourneyResponse> search(String source,
                                        String destination,
                                        LocalDate date) {

        List<Journey> journeys =
                journeyRepo.findBySourceAndDestinationAndJourneyDateAndStatus(
                        source,
                        destination,
                        date,
                        JourneyStatus.SCHEDULED
                );

        return journeys.stream()
                .map(this::map)
                .toList();
    }

    @Transactional
    public void cancelJourney(Long id) {

        Journey journey = journeyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Journey not found"));

        if(journey.getStatus() == JourneyStatus.CANCELLED){
            log.warn("Journey already cancelled");
            throw new IllegalArgumentException("Journey already cancelled");
        }

        if(journey.getStatus() == JourneyStatus.COMPLETED){
            log.warn("Journey already cancelled");
            throw new IllegalArgumentException("Journey already completed");
        }

        journey.setStatus(JourneyStatus.CANCELLED);

        journeyRepo.save(journey);
    }

    public BigDecimal getBaseFare(Long id) {

        Journey journey = journeyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Journey not found"));

        if (journey.getBaseFare() == null) {
            throw new RuntimeException("Base fare not configured for journey");
        }

        return journey.getBaseFare();
    }
    public List<JourneySeat> getSeats(Long journeyId) {
        return seatRepo.findByJourneyId(journeyId);
    }
}