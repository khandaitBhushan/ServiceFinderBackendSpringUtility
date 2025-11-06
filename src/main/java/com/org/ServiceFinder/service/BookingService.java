package com.org.ServiceFinder.service;

import com.org.ServiceFinder.model.Booking;
import com.org.ServiceFinder.model.User;
import com.org.ServiceFinder.repository.BookingRepository;
import com.org.ServiceFinder.repository.ServiceProviderRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ServiceProviderRepository providerRepository;
    private final UserService userService;
    private final ProviderServiceService serviceService; // NEW: Added dependency

    public BookingService(BookingRepository bookingRepository,
                          ServiceProviderRepository providerRepository,
                          UserService userService,
                          ProviderServiceService serviceService) { // NEW: Added parameter
        this.bookingRepository = bookingRepository;
        this.providerRepository = providerRepository;
        this.userService = userService;
        this.serviceService = serviceService; // NEW: Initialize
    }

    public Booking createBooking(Long providerId, LocalDateTime date) {
        User user = userService.getCurrentUser();
        var provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setServiceProvider(provider);
        booking.setDate(date);
        booking.setStatus("PENDING");

        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings() {
        User user = userService.getCurrentUser();
        return bookingRepository.findByUserId(user.getId());
    }

    public List<Booking> getProviderBookings() {
        User user = userService.getCurrentUser();
        var provider = providerRepository.findAll().stream()
                .filter(p -> p.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Provider not found for this user"));

        return bookingRepository.findByServiceProviderId(provider.getId());
    }

    public Booking updateBookingStatus(Long bookingId, String status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        User currentUser = userService.getCurrentUser();
        if (!booking.getServiceProvider().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to update this booking");
        }

        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public Booking createBookingWithService(Long providerId, Long serviceId, LocalDateTime date,
                                            String userNotes, String serviceAddress) {
        User user = userService.getCurrentUser();
        var provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        // Get the specific service using the serviceService
        var service = serviceService.getServicesByProvider(providerId).stream()
                .filter(s -> s.getId().equals(serviceId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Service not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setServiceProvider(provider);
        booking.setService(service);
        booking.setDate(date);
        booking.setStatus("PENDING");
        booking.setUserNotes(userNotes);
        booking.setServiceAddress(serviceAddress);
        booking.setFinalPrice(service.getPrice());
        booking.setDurationMinutes(service.getDurationMinutes());

        return bookingRepository.save(booking);
    }

    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        User currentUser = userService.getCurrentUser();
        if (!booking.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to cancel this booking");
        }

        booking.setStatus("CANCELLED");
        return bookingRepository.save(booking);
    }
}