package com.ey.pricing_service.dto;

import lombok.Data;

@Data
public class JourneySeatResponse {

    private Long id;
    private Long journeyId;
    private String seatNumber;

    private String seatClass;
    private String seatType;
    private String status;
}
