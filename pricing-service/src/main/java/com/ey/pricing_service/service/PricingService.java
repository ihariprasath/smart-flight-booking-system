package com.ey.pricing_service.service;

import com.ey.pricing_service.client.JourneyClient;
import com.ey.pricing_service.dto.PriceRequest;
import com.ey.pricing_service.dto.PriceResponse;
import com.ey.pricing_service.dto.RefundResponse;
import com.ey.pricing_service.entity.LedgerType;
import com.ey.pricing_service.entity.PricingLedger;
import com.ey.pricing_service.entity.SeatClass;
import com.ey.pricing_service.entity.SeatType;
import com.ey.pricing_service.exception.PricingException;
import com.ey.pricing_service.repository.PricingLedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final JourneyClient journeyClient;
    private final PricingLedgerRepository ledgerRepository;

    public PriceResponse calculatePrice(PriceRequest request) {

        var journey = journeyClient.getJourney(request.getJourneyId());

        if (journey == null) {
            throw new PricingException("Journey not found");
        }

        BigDecimal baseFare = journey.getBaseFare();


        BigDecimal occupancy =
                BigDecimal.valueOf(journey.getTotalSeats() - journey.getAvailableSeats())
                        .divide(BigDecimal.valueOf(journey.getTotalSeats()), 4, RoundingMode.HALF_UP);

        BigDecimal occupancyMultiplier = BigDecimal.ONE;

        if (occupancy.compareTo(new BigDecimal("0.70")) > 0) {
            occupancyMultiplier = new BigDecimal("1.4");
        } else if (occupancy.compareTo(new BigDecimal("0.50")) > 0) {
            occupancyMultiplier = new BigDecimal("1.2");
        }


        LocalDateTime departure =
                journey.getJourneyDate().atTime(journey.getDepartureTime());

        long hoursToDeparture =
                Duration.between(LocalDateTime.now(), departure).toHours();

        BigDecimal timeMultiplier = BigDecimal.ONE;

        if (hoursToDeparture < 24) {
            timeMultiplier = new BigDecimal("1.6");
        } else if (hoursToDeparture < 72) {
            timeMultiplier = new BigDecimal("1.4");
        } else if (hoursToDeparture < 168) {
            timeMultiplier = new BigDecimal("1.2");
        }


        BigDecimal surgeMultiplier =
                occupancyMultiplier.multiply(timeMultiplier);

        BigDecimal surgeFare =
                baseFare.multiply(surgeMultiplier);


        if (request.getSeatClass() == SeatClass.BUSINESS) {
            surgeFare = surgeFare.multiply(new BigDecimal("1.8"));
        }
        else{
            surgeFare = journey.getBaseFare();
        }


        if (request.getSeatType() == SeatType.WINDOW) {
            surgeFare = surgeFare.add(new BigDecimal("500"));
        } else if (request.getSeatType() == SeatType.AISLE) {
            surgeFare = surgeFare.add(new BigDecimal("300"));
        }

        BigDecimal gstRate =
                (request.getSeatClass() == SeatClass.BUSINESS)
                        ? new BigDecimal("0.12")
                        : new BigDecimal("0.05");

        BigDecimal gstAmount =
                surgeFare.multiply(gstRate).setScale(2, RoundingMode.HALF_UP);

        BigDecimal finalAmount =
                surgeFare.add(gstAmount).setScale(2, RoundingMode.HALF_UP);


        PricingLedger entry = PricingLedger.builder()
                .bookingId(request.getBookingId())
                .journeyId(request.getJourneyId())
                .baseFare(baseFare)
                .seatClass(request.getSeatClass())
                .seatType(request.getSeatType())
                .gstAmount(gstAmount)
                .totalAmount(finalAmount)
                .ledgerType(LedgerType.PAYMENT)
                .build();

        ledgerRepository.save(entry);

        return PriceResponse.builder()
                .bookingId(request.getBookingId())
                .journeyId(request.getJourneyId())
                .baseFare(baseFare)
                .surgeFare(surgeFare)
                .gstAmount(gstAmount)
                .totalAmount(finalAmount)
                .build();
    }


    public RefundResponse calculateRefund(Long bookingId) {

        PricingLedger payment =
                ledgerRepository.findTopByBookingIdAndLedgerTypeOrderByIdDesc(
                        bookingId, LedgerType.PAYMENT
                ).orElseThrow(() -> new PricingException("Payment record not found"));

        BigDecimal paidAmount = payment.getTotalAmount();


        BigDecimal withoutGST =
                paidAmount.subtract(payment.getGstAmount());

        BigDecimal cancellationFee =
                withoutGST.multiply(BigDecimal.valueOf(0.25));

        BigDecimal refundAmount =
                withoutGST.subtract(cancellationFee)
                        .setScale(2, RoundingMode.HALF_UP);


        PricingLedger refundLedger = PricingLedger.builder()
                .bookingId(bookingId)
                .journeyId(payment.getJourneyId())
                .baseFare(payment.getBaseFare())
                .seatClass(payment.getSeatClass())
                .seatType(payment.getSeatType())
                .gstAmount(payment.getGstAmount())
                .totalAmount(refundAmount)
                .ledgerType(LedgerType.REFUND)
                .build();

        ledgerRepository.save(refundLedger);

        return RefundResponse.builder()
                .bookingId(bookingId)
                .refundAmount(refundAmount)
                .cancellationFee(cancellationFee)
                .build();
    }
}