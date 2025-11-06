package com.org.ServiceFinder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_providers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ServiceCategory serviceCategory;

    private String description;
    private Integer experienceYears;
    private Double rating = 0.0;
    private String location;
    private Double latitude;
    private Double longitude;

    // ADD BACK: Pricing fields
    private Double hourlyRate;
    private Double minServicePrice;
    private String pricingDescription;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    // ADD BACK: Services list (with JsonIgnore to avoid recursion)
    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProviderService> services = new ArrayList<>();
}