package com.ey.booking_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatInfoResponse {

    private String seatNumber;
    private String seatClass;
    private boolean available;
}
