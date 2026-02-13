package com.ey.pricing_service.dto;

import com.ey.pricing_service.entity.SeatClass;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RefundRequest {

    @NotNull
    private Long bookingId;
}
