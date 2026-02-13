package com.ey.booking_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PassengerResponse {

    private String name;
    private int age;
    private String gender;
    private String seatNumber;
}
