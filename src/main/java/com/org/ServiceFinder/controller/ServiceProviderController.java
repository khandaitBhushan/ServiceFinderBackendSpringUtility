package com.org.ServiceFinder.controller;

import com.org.ServiceFinder.dto.ProviderWithDistance;
import com.org.ServiceFinder.model.ServiceProvider;
import com.org.ServiceFinder.model.ServiceCategory;
import com.org.ServiceFinder.service.ServiceProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/providers")
public class ServiceProviderController {

    private final ServiceProviderService providerService;

    public ServiceProviderController(ServiceProviderService providerService) {
        this.providerService = providerService;
    }

    // Register new service provider
    @PostMapping("/register")
    public ResponseEntity<ServiceProvider> registerProvider(@RequestBody ServiceProvider provider) {
        ServiceProvider savedProvider = providerService.registerProvider(provider);
        return ResponseEntity.ok(savedProvider);
    }

    // Get all providers
    @GetMapping
    public ResponseEntity<List<ServiceProvider>> getAllProviders() {
        List<ServiceProvider> providers = providerService.getAllProviders();
        return ResponseEntity.ok(providers);
    }

    // Get provider by ID
    @GetMapping("/{id}")
    public ResponseEntity<ServiceProvider> getProviderById(@PathVariable Long id) {
        return providerService.getProviderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Basic search by category and/or location name
    @GetMapping("/search")
    public ResponseEntity<List<ServiceProvider>> searchProviders(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location) {
        List<ServiceProvider> providers = providerService.searchProviders(category, location);
        return ResponseEntity.ok(providers);
    }

    // NEW: Search within radius (kilometers)
    @GetMapping("/search/radius")
    public ResponseEntity<List<ServiceProvider>> searchWithinRadius(
            @RequestParam(required = false) String category,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double radiusKm) {

        List<ServiceProvider> providers = providerService.searchProvidersWithinRadius(
                category, latitude, longitude, radiusKm
        );
        return ResponseEntity.ok(providers);
    }

    // NEW: Get providers with distance info
    @GetMapping("/with-distance")
    public ResponseEntity<List<ProviderWithDistance>> getProvidersWithDistance(
            @RequestParam(required = false) String category,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {

        List<ProviderWithDistance> providers = providerService.getProvidersWithDistance(
                category, latitude, longitude
        );
        return ResponseEntity.ok(providers);
    }

    // NEW: Get nearby providers sorted by distance
    @GetMapping("/nearby")
    public ResponseEntity<List<ProviderWithDistance>> getNearbyProviders(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(required = false) Double maxDistance) {

        List<ProviderWithDistance> providers = providerService.getNearbyProviders(
                latitude, longitude, maxDistance
        );
        return ResponseEntity.ok(providers);
    }

    // NEW: Get nearby providers by category sorted by distance
    @GetMapping("/nearby/category")
    public ResponseEntity<List<ProviderWithDistance>> getNearbyProvidersByCategory(
            @RequestParam(required = false) String category,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(required = false) Double maxDistance) {

        List<ProviderWithDistance> providers = providerService.getNearbyProvidersByCategory(
                category, latitude, longitude, maxDistance
        );
        return ResponseEntity.ok(providers);
    }

    // Create new service category
    @PostMapping("/categories")
    public ResponseEntity<ServiceCategory> createCategory(@RequestParam String name) {
        ServiceCategory category = providerService.createCategory(name);
        return ResponseEntity.ok(category);
    }

    // Get all categories
    @GetMapping("/categories")
    public ResponseEntity<List<ServiceCategory>> getAllCategories() {
        List<ServiceCategory> categories = providerService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}