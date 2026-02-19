package com.ey.booking_service.dto;

import com.ey.booking_service.entity.SeatClass;
import com.ey.booking_service.entity.SeatType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
@Builder
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {

    @NotNull
    private Long journeyId;
    private SeatClass seatClass;
    @NotEmpty
    private List<PassengerRequest> passengers;
}
