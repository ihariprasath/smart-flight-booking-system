package com.ey.booking_service.dto;

import com.ey.booking_service.entity.BookingStatus;
import com.ey.booking_service.entity.SeatClass;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Data
@Builder
public class BookingResponse {

    private Long id;
    private String bookingRef;

    private Long journeyId;
    private List<String> seatNumbers;
    private SeatClass seatClass;
    private List<PassengerResponse> passengers;
    private BigDecimal totalAmount;
    private BigDecimal refundAmount;
    private BookingStatus status;
}
