package com.ey.ticket_service.client;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "booking-service")
public interface BookingClient {

    @GetMapping("/bookings/{id}")
    BookingDto get(@PathVariable("id") Long id);

   // @PutMapping("/bookings/cancel/{id}")
   // void cancelBooking(@PathVariable("id") Long id);

    @Data
    class BookingDto{
        private Long id;
        private String passengerName;
    }
}
