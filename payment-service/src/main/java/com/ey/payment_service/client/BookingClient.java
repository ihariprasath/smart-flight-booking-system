package com.ey.payment_service.client;

import com.ey.payment_service.dto.BookingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "booking-service")

public interface BookingClient {

    @GetMapping("/bookings/{id}")
    BookingResponse getBooking(@PathVariable Long id);

    @PutMapping("/bookings/{bookingId}/confirm")
    void confirmBooking(@PathVariable Long id);

    @PutMapping("/bookings/{bookingId}/cancel")
    void cancelBooking(@PathVariable Long id);

    @PutMapping("/bookings/{id}/fail")
    void failBooking(@PathVariable Long id);
}
