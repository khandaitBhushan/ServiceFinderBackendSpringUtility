package com.org.ServiceFinder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "provider_services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;
    private String description;
    private Double price;
    private Integer durationMinutes; // Service duration in minutes
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    @JsonIgnore
    private ServiceProvider provider;
}