package com.ey.booking_service.repository;

import com.ey.booking_service.entity.Booking;
import com.ey.booking_service.entity.BookingStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void save_success(){
        Booking booking = Booking.builder()
                .status(BookingStatus.PENDING)
                .build();

        Booking saved = bookingRepository.save(booking);

        assertNotNull(saved.getId());
    }
}
