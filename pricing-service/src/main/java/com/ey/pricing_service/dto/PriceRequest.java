package com.ey.pricing_service.dto;

import com.ey.pricing_service.entity.SeatClass;
import com.ey.pricing_service.entity.SeatType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PriceRequest {

    @NotNull
    private Long bookingId;
    @NotNull
    private Long journeyId;

    @NotNull
    private SeatClass seatClass;

    @NotNull
    private SeatType seatType;
}
