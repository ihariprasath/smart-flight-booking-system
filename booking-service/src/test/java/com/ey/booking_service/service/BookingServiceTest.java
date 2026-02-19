package com.ey.booking_service.service;

import com.ey.booking_service.client.JourneyClient;
import com.ey.booking_service.client.PricingClient;
import com.ey.booking_service.dto.*;
import com.ey.booking_service.entity.*;
import com.ey.booking_service.exception.BookingException;
import com.ey.booking_service.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private JourneyClient journeyClient;

    @Mock
    private PricingClient pricingClient;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void createBooking_success() {

        BookingRequest request = new BookingRequest();
        request.setJourneyId(1L);
        request.setSeatClass(SeatClass.ECONOMY);

        PassengerRequest p = new PassengerRequest();
        p.setName("Hari");
        p.setAge(25);
        p.setGender("M");
        p.setSeatNumber("A1");

        request.setPassengers(List.of(p));

        SeatInfoResponse seatInfo =
                new SeatInfoResponse("A1", "ECONOMY", true);

        when(journeyClient.getSeatInfo(1L, "A1")).thenReturn(seatInfo);

        PriceResponse priceResponse =
                PriceResponse.builder()
                        .totalAmount(BigDecimal.valueOf(1000))
                        .build();

        when(pricingClient.calculate(any())).thenReturn(priceResponse);

        Booking saved = Booking.builder()
                .id(1L)
                .status(BookingStatus.PENDING)
                .build();

        when(bookingRepository.save(any())).thenReturn(saved);

        BookingResponse response = bookingService.createBooking(request);

        assertNotNull(response);
        verify(journeyClient).lockSeats(eq(1L), anyLong(), any());
    }

    @Test
    void confirmBooking_success() {

        Booking booking = Booking.builder()
                .id(1L)
                .journeyId(1L)
                .seatNumbers("A1")
                .status(BookingStatus.PENDING)
                .build();

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        when(bookingRepository.save(any()))
                .thenReturn(booking);

        BookingResponse response = bookingService.confirmBooking(1L);

        assertEquals(BookingStatus.CONFIRMED, response.getStatus());
        verify(journeyClient).confirmSeats(eq(1L), eq(1L), any());
    }

    @Test
    void cancelBooking_alreadyCancelled() {

        Booking booking = Booking.builder()
                .id(1L)
                .status(BookingStatus.CANCELLED)
                .build();

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        assertThrows(
                BookingException.class,
                () -> bookingService.cancelBooking(1L)
        );
    }
    
    @Test
    void getById_success() {

        Booking booking = Booking.builder()
                .id(1L)
                .status(BookingStatus.CONFIRMED)
                .build();

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        BookingResponse response = bookingService.getById(1L);

        assertEquals(1L, response.getId());
    }
}