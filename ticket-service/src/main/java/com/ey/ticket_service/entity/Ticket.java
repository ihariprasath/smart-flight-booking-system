package com.ey.ticket_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ticketNumber;

    @Column(nullable = false)
    private Long bookingId;

    @Column(nullable = false)
    private Long paymentId;

    @Column(nullable = false)
    private Long passengerName;

    @Column(nullable = false)
    private String seatNumbers;

    private String pdfPath;

    private LocalDateTime createdAt;
}
