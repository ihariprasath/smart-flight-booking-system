package com.ey.booking_service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PassengerRequest {

    private String name;
    private Integer age;
    private String gender;
    private String seatNumbers;
}
