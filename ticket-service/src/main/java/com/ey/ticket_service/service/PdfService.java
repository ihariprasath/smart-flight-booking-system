package com.ey.ticket_service.service;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;

@Service
public class PdfService {

    public String generateTicketPdf(String ticketNumber,
                                    Long bookingId,
                                    String passenger,
                                    String seats) {

        try {
            String folder = "tickets/";
            new File(folder).mkdirs();

            String path = folder + ticketNumber + ".pdf";

            PdfWriter writer = new PdfWriter(path);
            Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

            document.add(new Paragraph("FLIGHT TICKET"));
            document.add(new Paragraph("Ticket No: " + ticketNumber));
            document.add(new Paragraph("Booking ID: " + bookingId));
            document.add(new Paragraph("Passenger: " + passenger));
            document.add(new Paragraph("Seats: " + seats));

            document.close();

            return path;

        } catch (FileNotFoundException e) {
            throw new RuntimeException("PDF generation failed");
        }
    }
}