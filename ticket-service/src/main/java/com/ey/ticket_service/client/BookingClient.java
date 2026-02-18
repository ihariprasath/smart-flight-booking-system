package com.ey.ticket_service.client;

import com.ey.ticket_service.dto.BookingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "booking-service")
public interface BookingClient {

    @GetMapping("/bookings/{id}")
    BookingResponse getById(@PathVariable Long id);
}
