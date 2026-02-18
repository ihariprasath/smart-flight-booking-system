package com.ey.booking_service.client;

import com.ey.booking_service.dto.SeatInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "journey-service")
public interface JourneyClient {

    @PutMapping("/journeys/{journeyId}/seats/lock")
    void lockSeats(@PathVariable Long journeyId,
                   @RequestParam Long bookingId,
                   @RequestBody List<String> seatNumbers);

    @PutMapping("/journeys/{journeyId}/seats/confirm")
    void confirmSeats(@PathVariable Long journeyId,
                      @RequestParam Long bookingId,
                      @RequestBody List<String> seatNumbers);

    @PutMapping("/journeys/{journeyId}/seats/release")
    void releaseSeats(@PathVariable Long journeyId,
                      @RequestParam Long bookingId,
                      @RequestBody List<String> seatNumbers);

    @GetMapping("/journeys/{journeyId}/seats/info")
    SeatInfoResponse getSeatInfo(
            @PathVariable Long journeyId,
            @RequestParam String seatNumber
    );

}
