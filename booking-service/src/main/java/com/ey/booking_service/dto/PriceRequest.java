package com.ey.booking_service.dto;

import com.ey.booking_service.entity.SeatClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PriceRequest {

    private Long journeyId;
    private SeatClass seatClass;
    private String seatNumber;
}
