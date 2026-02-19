package com.ey.booking_service.controller;

import com.ey.booking_service.dto.BookingRequest;
import com.ey.booking_service.dto.BookingResponse;
import com.ey.booking_service.dto.PassengerRequest;
import com.ey.booking_service.entity.BookingStatus;
import com.ey.booking_service.entity.SeatClass;
import com.ey.booking_service.security.JwtAuthFilter;
import com.ey.booking_service.security.JwtUtil;
import com.ey.booking_service.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
})
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    void createBooking_success() throws Exception {

        BookingRequest request = new BookingRequest();
        request.setJourneyId(9L);
        request.setSeatClass(SeatClass.ECONOMY);

        PassengerRequest p = new PassengerRequest();
        p.setName("Hari");
        p.setAge(25);
        p.setGender("Male");
        p.setSeatNumber("20A");

        request.setPassengers(List.of(p));

        BookingResponse response = BookingResponse.builder()
                .id(1L)
                .status(BookingStatus.PENDING)
                .totalAmount(BigDecimal.valueOf(1000))
                .build();

        when(bookingService.createBooking(request)).thenReturn(response);

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBooking_success() throws Exception {

        BookingResponse response = BookingResponse.builder()
                .id(32L)
                .bookingRef("REF123")
                .journeyId(1L)
                .seatNumbers(List.of("A1"))
                .seatClass(SeatClass.ECONOMY)
                .totalAmount(BigDecimal.valueOf(500))
                .status(BookingStatus.CONFIRMED)
                .build();

        when(bookingService.getById(32L)).thenReturn(response);

        mockMvc.perform(get("/bookings/32")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(32L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void confirmBooking_success() throws Exception {

        BookingResponse response = BookingResponse.builder()
                .id(1L)
                .status(BookingStatus.CONFIRMED)
                .build();

        when(bookingService.confirmBooking(1L)).thenReturn(response);

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/bookings/32/confirm"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void cancelBooking_success() throws Exception {

        BookingResponse response = BookingResponse.builder()
                .id(1L)
                .status(BookingStatus.CANCELLED)
                .build();

        when(bookingService.cancelBooking(1L)).thenReturn(response);

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/bookings/32/cancel"))
                .andExpect(status().isOk());
    }
}