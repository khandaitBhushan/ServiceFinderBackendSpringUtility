package com.org.ServiceFinder.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderDTO {
    private Long id;
    private String companyName;
    private String serviceCategory;
    private String description;
    private Integer experienceYears;
    private Double rating;
    private String location;
    private String userEmail;
}