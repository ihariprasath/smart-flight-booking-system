package com.ey.booking_service.dto;

import com.ey.booking_service.entity.SeatClass;
import lombok.*;

import java.math.BigDecimal;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefundRequest {

    private Long bookingId;
}
