package com.ey.booking_service.controller;

import com.ey.booking_service.dto.*;
import com.ey.booking_service.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponse create(@RequestBody BookingRequest request) {
        return bookingService.createBooking(request);
    }

    @PutMapping("/{id}/confirm")
    public BookingResponse confirm(@PathVariable Long id) {
        return bookingService.confirmBooking(id);
    }

    @PutMapping("/{id}/cancel")
    public BookingResponse cancel(@PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }

    @PutMapping("/{id}/fail")
    public void fail(@PathVariable Long id){
        bookingService.failBooking(id);
    }

    @GetMapping("/{id}")
    public BookingResponse getById(@PathVariable Long id) {
        return bookingService.getById(id);
    }
}