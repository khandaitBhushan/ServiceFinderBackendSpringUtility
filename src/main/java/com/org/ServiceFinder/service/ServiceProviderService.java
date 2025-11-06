package com.org.ServiceFinder.service;

import com.org.ServiceFinder.dto.ProviderWithDistance;
import com.org.ServiceFinder.model.ServiceProvider;
import com.org.ServiceFinder.model.ServiceCategory;
import com.org.ServiceFinder.model.User;
import com.org.ServiceFinder.repository.ServiceProviderRepository;
import com.org.ServiceFinder.repository.ServiceCategoryRepository;
import com.org.ServiceFinder.util.LocationUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceProviderService {

    private final ServiceProviderRepository providerRepository;
    private final ServiceCategoryRepository categoryRepository;
    private final UserService userService;
    private final LocationUtil locationUtil;

    public ServiceProviderService(ServiceProviderRepository providerRepository,
                                  ServiceCategoryRepository categoryRepository,
                                  UserService userService,
                                  LocationUtil locationUtil) {
        this.providerRepository = providerRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
        this.locationUtil = locationUtil;
    }

    // Register new service provider
    public ServiceProvider registerProvider(ServiceProvider provider) {
        User currentUser = userService.getCurrentUser();
        provider.setUser(currentUser);
        return providerRepository.save(provider);
    }

    // Get all providers
    public List<ServiceProvider> getAllProviders() {
        return providerRepository.findAll();
    }

    // Get provider by ID
    public Optional<ServiceProvider> getProviderById(Long id) {
        return providerRepository.findById(id);
    }

    // FIXED: Basic search by category and/or location name
    public List<ServiceProvider> searchProviders(String category, String location) {
        if (category != null) category = category.trim();
        if (location != null) location = location.trim();

        // Use the fixed repository method
        return providerRepository.searchProviders(category, location);
    }

    // FIXED: Search providers within radius
    public List<ServiceProvider> searchProvidersWithinRadius(String category, Double userLat, Double userLon, Double radiusKm) {
        List<ServiceProvider> allProviders;

        if (category != null && !category.trim().isEmpty()) {
            // Use the new repository method
            allProviders = providerRepository.findByServiceCategoryNameContaining(category.trim());
        } else {
            allProviders = providerRepository.findAll();
        }

        // Filter by distance
        return allProviders.stream()
                .filter(provider -> {
                    if (provider.getLatitude() == null || provider.getLongitude() == null)
                        return false;

                    double distance = locationUtil.calculateDistance(
                            userLat, userLon,
                            provider.getLatitude(), provider.getLongitude()
                    );
                    return distance <= radiusKm;
                })
                .collect(Collectors.toList());
    }

    public Optional<ServiceProvider> getProviderByUserId(Long userId) {
        return providerRepository.findAll().stream()
                .filter(provider -> provider.getUser().getId().equals(userId))
                .findFirst();
    }

    // FIXED: Get providers with distance information
    public List<ProviderWithDistance> getProvidersWithDistance(String category, Double userLat, Double userLon) {
        List<ServiceProvider> providers;

        if (category != null && !category.trim().isEmpty()) {
            providers = providerRepository.findByServiceCategoryNameContaining(category.trim());
        } else {
            providers = providerRepository.findAll();
        }

        return providers.stream()
                .map(provider -> {
                    Double distance = null;
                    if (provider.getLatitude() != null && provider.getLongitude() != null) {
                        distance = locationUtil.calculateDistance(
                                userLat, userLon,
                                provider.getLatitude(), provider.getLongitude()
                        );
                        // Round to 2 decimal places
                        distance = Math.round(distance * 100.0) / 100.0;
                    }

                    return new ProviderWithDistance(provider, distance);
                })
                .collect(Collectors.toList());
    }

    // FIXED: Get nearby providers sorted by distance
    public List<ProviderWithDistance> getNearbyProviders(Double userLat, Double userLon, Double maxDistance) {
        List<ServiceProvider> allProviders = providerRepository.findAll();

        return allProviders.stream()
                .map(provider -> {
                    if (provider.getLatitude() == null || provider.getLongitude() == null)
                        return null;

                    double distance = locationUtil.calculateDistance(
                            userLat, userLon,
                            provider.getLatitude(), provider.getLongitude()
                    );

                    // Round to 2 decimal places
                    distance = Math.round(distance * 100.0) / 100.0;

                    if (maxDistance == null || distance <= maxDistance) {
                        return new ProviderWithDistance(provider, distance);
                    }
                    return null;
                })
                .filter(p -> p != null)
                .sorted((p1, p2) -> Double.compare(p1.getDistance(), p2.getDistance()))
                .collect(Collectors.toList());
    }

    // FIXED: Get nearby providers by category sorted by distance
    public List<ProviderWithDistance> getNearbyProvidersByCategory(String category, Double userLat, Double userLon, Double maxDistance) {
        List<ServiceProvider> providers;

        if (category != null && !category.trim().isEmpty()) {
            providers = providerRepository.findByServiceCategoryNameContaining(category.trim());
        } else {
            providers = providerRepository.findAll();
        }

        return providers.stream()
                .map(provider -> {
                    if (provider.getLatitude() == null || provider.getLongitude() == null)
                        return null;

                    double distance = locationUtil.calculateDistance(
                            userLat, userLon,
                            provider.getLatitude(), provider.getLongitude()
                    );

                    // Round to 2 decimal places
                    distance = Math.round(distance * 100.0) / 100.0;

                    if (maxDistance == null || distance <= maxDistance) {
                        return new ProviderWithDistance(provider, distance);
                    }
                    return null;
                })
                .filter(p -> p != null)
                .sorted((p1, p2) -> Double.compare(p1.getDistance(), p2.getDistance()))
                .collect(Collectors.toList());
    }

    // Create new service category
    public ServiceCategory createCategory(String name) {
        ServiceCategory category = new ServiceCategory();
        category.setName(name);
        return categoryRepository.save(category);
    }

    // Get all categories
    public List<ServiceCategory> getAllCategories() {
        return categoryRepository.findAll();
    }
}