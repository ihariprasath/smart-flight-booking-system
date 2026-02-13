package com.ey.booking_service.dto;

import com.ey.booking_service.entity.SeatClass;
import com.ey.booking_service.entity.SeatType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Builder
@Getter
@Setter
@Data
public class BookingRequest {

    private Long journeyId;
    private SeatClass seatClass;
    private List<PassengerRequest> passengers;
}
