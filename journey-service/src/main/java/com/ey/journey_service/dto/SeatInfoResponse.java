package com.ey.journey_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatInfoResponse {

    private String seatNumber;
    private String seatClass;
    private boolean available;
}
