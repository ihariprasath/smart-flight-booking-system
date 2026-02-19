package com.ey.booking_service.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PriceResponse {

    private java.math.BigDecimal totalAmount;

}
