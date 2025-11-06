package com.org.ServiceFinder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    @JsonIgnore
    private ServiceProvider serviceProvider;

    // ADD: Specific service booked
    @ManyToOne
    @JoinColumn(name = "service_id")
    private ProviderService service;

    private LocalDateTime date;
    private String status;

    // ADD: Pricing fields
    private Double finalPrice;
    private Integer durationMinutes;
    private String userNotes;
    private String serviceAddress;

}