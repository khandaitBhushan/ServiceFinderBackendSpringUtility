package com.org.ServiceFinder.service;

import com.org.ServiceFinder.model.ProviderService;
import com.org.ServiceFinder.model.ServiceProvider;
import com.org.ServiceFinder.model.User;
import com.org.ServiceFinder.repository.ProviderServiceRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProviderServiceService {

    private final ProviderServiceRepository serviceRepository;
    private final UserService userService;
    private final ServiceProviderService providerService;

    public ProviderServiceService(ProviderServiceRepository serviceRepository,
                                  UserService userService,
                                  ServiceProviderService providerService) {
        this.serviceRepository = serviceRepository;
        this.userService = userService;
        this.providerService = providerService;
    }

    // Add new service for provider
    public ProviderService addService(ProviderService service) {
        User currentUser = userService.getCurrentUser();
        ServiceProvider provider = providerService.getProviderByUserId(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        service.setProvider(provider);
        return serviceRepository.save(service);
    }

    // Get all services for current provider
    public List<ProviderService> getMyServices() {
        User currentUser = userService.getCurrentUser();
        ServiceProvider provider = providerService.getProviderByUserId(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        return serviceRepository.findByProviderId(provider.getId());
    }

    // Get services by provider ID (for customers)
    public List<ProviderService> getServicesByProvider(Long providerId) {
        return serviceRepository.findByProviderIdAndIsActiveTrue(providerId);
    }

    // SEARCH: Find services by name across all providers
    public List<ProviderService> searchServicesByName(String serviceName) {
        return serviceRepository.findByServiceNameContainingIgnoreCaseAndIsActiveTrue(serviceName);
    }

    // Get all active services for discovery
    public List<ProviderService> getAllActiveServices() {
        return serviceRepository.findAll().stream()
                .filter(ProviderService::getIsActive)
                .toList();
    }
}