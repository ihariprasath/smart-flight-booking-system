package com.ey.booking_service.service;

import com.ey.booking_service.client.JourneyClient;
import com.ey.booking_service.client.PricingClient;
import com.ey.booking_service.dto.*;
import com.ey.booking_service.entity.*;
import com.ey.booking_service.exception.BookingException;
import com.ey.booking_service.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final JourneyClient journeyClient;
    private final PricingClient pricingClient;


    @Transactional
    public BookingResponse createBooking(BookingRequest request) {

        if (request.getPassengers() == null || request.getPassengers().isEmpty()) {
            throw new BookingException("Passengers list cannot be empty");
        }

        List<String> seatNumbers = request.getPassengers()
                .stream()
                .map(PassengerRequest::getSeatNumbers)
                .collect(Collectors.toList());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (String seatNumber : seatNumbers) {

            PriceRequest priceRequest = PriceRequest.builder()
                    .journeyId(request.getJourneyId())
                    .seatNumber(seatNumber)
                    .build();

            PriceResponse priceResponse = pricingClient.calculate(priceRequest);

            totalAmount = totalAmount.add(priceResponse.getTotalAmount());
        }

        Booking booking = Booking.builder()
                .bookingRef(UUID.randomUUID().toString())
                .journeyId(request.getJourneyId())
                .seatNumbers(seatNumbers)
                .seatClass(request.getSeatClass())
                .totalAmount(totalAmount)
                .status(BookingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .lockExpiryTime(LocalDateTime.now().plusMinutes(5))
                .build();

        booking = bookingRepository.save(booking); // Save first


        List<Passenger> passengers = new ArrayList<>();

        for (PassengerRequest p : request.getPassengers()) {

            Passenger passenger = Passenger.builder()
                    .name(p.getName())
                    .age(p.getAge())
                    .gender(p.getGender())
                    .seatNumbers(p.getSeatNumbers())
                    .build();

            passenger.setBooking(booking);

            passengers.add(passenger);
        }

        booking.setPassengers(passengers);

        booking = bookingRepository.save(booking);

        journeyClient.lockSeats(
                request.getJourneyId(),
                booking.getId(),
                seatNumbers
        );

        return mapToResponse(booking);
    }

    @Transactional
    public BookingResponse confirmBooking(Long bookingId) {

        Booking booking = getBooking(bookingId);

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BookingException("Only pending booking can be confirmed");
        }


        journeyClient.confirmSeats(
                booking.getJourneyId(),
                booking.getSeatNumbers()
        );

        booking.setStatus(BookingStatus.CONFIRMED);

        return mapToResponse(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponse cancelBooking(Long bookingId) {

        Booking booking = getBooking(bookingId);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingException("Booking already cancelled");
        }


        journeyClient.releaseSeats(
                booking.getJourneyId(),
                booking.getId(),
                booking.getSeatNumbers()
        );

        RefundRequest refundRequest = new RefundRequest(bookingId);

        RefundResponse refund =
                pricingClient.calculateRefund(refundRequest);

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setRefundAmount(refund.getRefundAmount());

        return mapToResponse(bookingRepository.save(booking));
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void expirePendingBookings() {

        List<Booking> expiredBookings =
                bookingRepository.findByStatusAndLockExpiryTimeBefore(
                        BookingStatus.PENDING,
                        LocalDateTime.now()
                );

        for (Booking booking : expiredBookings) {

            journeyClient.releaseSeats(
                    booking.getJourneyId(),
                    booking.getId(),
                    booking.getSeatNumbers()
            );

            booking.setStatus(BookingStatus.EXPIRED);
            bookingRepository.save(booking);
        }
    }

    public BookingResponse getById(Long bookingId) {
        return mapToResponse(getBooking(bookingId));
    }

    private Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingException("Booking not found"));
    }

    private BookingResponse mapToResponse(Booking booking) {

        List<PassengerResponse> passengersResponses = booking.getPassengers()
                .stream()
                .map(p -> PassengerResponse.builder()
                        .name(p.getName())
                        .age(p.getAge())
                        .gender(p.getGender())
                        .seatNumber(p.getSeatNumbers())
                        .build())
                .toList();

        return BookingResponse.builder()
                .id(booking.getId())
                .bookingRef(booking.getBookingRef())
                .journeyId(booking.getJourneyId())
                .seatNumbers(booking.getSeatNumbers())
                .seatClass(booking.getSeatClass())
                .passengers(passengersResponses)
                .totalAmount(booking.getTotalAmount())
                .refundAmount(booking.getRefundAmount())
                .status(booking.getStatus())
                .build();
    }
}