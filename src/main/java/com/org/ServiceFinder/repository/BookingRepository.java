package com.org.ServiceFinder.repository;

import com.org.ServiceFinder.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByServiceProviderId(Long providerId);
}