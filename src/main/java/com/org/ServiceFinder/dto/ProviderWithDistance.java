package com.org.ServiceFinder.dto;

import com.org.ServiceFinder.model.ServiceProvider;
import lombok.Data;

@Data
public class ProviderWithDistance {
    private ServiceProvider provider;
    private Double distance; // in kilometers

    public ProviderWithDistance(ServiceProvider provider, Double distance) {
        this.provider = provider;
        this.distance = distance;
    }
}