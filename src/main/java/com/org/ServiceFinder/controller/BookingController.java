package com.org.ServiceFinder.controller;

import com.org.ServiceFinder.model.Booking;
import com.org.ServiceFinder.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestParam Long providerId,
                                                 @RequestParam String dateTime) {
        LocalDateTime date = LocalDateTime.parse(dateTime);
        Booking booking = bookingService.createBooking(providerId, date);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Booking>> getUserBookings() {
        List<Booking> bookings = bookingService.getUserBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/provider")
    public ResponseEntity<List<Booking>> getProviderBookings() {
        List<Booking> bookings = bookingService.getProviderBookings();
        return ResponseEntity.ok(bookings);
    }



    @PutMapping("/{id}/status")
//    @PreAuthorize("hasRole('PROVIDER')")  // ADD THIS
    public ResponseEntity<Booking> updateBookingStatus(@PathVariable Long id,
                                                       @RequestParam String status) {
        Booking booking = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{id}/cancel")
//    @PreAuthorize("hasRole('USER') or hasRole('PROVIDER')")  // ADD THIS
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id) {
            Booking booking = bookingService.cancelBooking(id);
            return ResponseEntity.ok(booking);
    }

    @PostMapping("/with-service")
    public ResponseEntity<Booking> createBookingWithService(
            @RequestParam Long providerId,
            @RequestParam Long serviceId,
            @RequestParam String dateTime,
            @RequestParam String userNotes,
            @RequestParam String serviceAddress) {

        LocalDateTime date = LocalDateTime.parse(dateTime);
        Booking booking = bookingService.createBookingWithService(
                providerId, serviceId, date, userNotes, serviceAddress
        );
        return ResponseEntity.ok(booking);
    }
}