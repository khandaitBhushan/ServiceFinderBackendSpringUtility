package com.org.ServiceFinder.model;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long providerId;
    private String name; // Electrician, Plumber
    private String description;
    private Double price;
}

