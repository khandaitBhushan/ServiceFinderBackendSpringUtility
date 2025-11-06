package com.org.ServiceFinder.controller;

import com.org.ServiceFinder.model.ProviderService;
import com.org.ServiceFinder.service.ProviderServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/provider-services")
public class ProviderServiceController {

    private final ProviderServiceService serviceService;

    public ProviderServiceController(ProviderServiceService serviceService) {
        this.serviceService = serviceService;
    }

    // Provider adds new service
    @PostMapping
    public ResponseEntity<ProviderService> addService(@RequestBody ProviderService service) {
        ProviderService savedService = serviceService.addService(service);
        return ResponseEntity.ok(savedService);
    }

    // Provider views their services
    @GetMapping("/my-services")
    public ResponseEntity<List<ProviderService>> getMyServices() {
        List<ProviderService> services = serviceService.getMyServices();
        return ResponseEntity.ok(services);
    }

    // Customers view provider's services
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<ProviderService>> getProviderServices(@PathVariable Long providerId) {
        List<ProviderService> services = serviceService.getServicesByProvider(providerId);
        return ResponseEntity.ok(services);
    }

    // SEARCH: Search services by name
    @GetMapping("/search")
    public ResponseEntity<List<ProviderService>> searchServices(@RequestParam String serviceName) {
        List<ProviderService> services = serviceService.searchServicesByName(serviceName);
        return ResponseEntity.ok(services);
    }

    // Get all services for discovery
    @GetMapping
    public ResponseEntity<List<ProviderService>> getAllServices() {
        List<ProviderService> services = serviceService.getAllActiveServices();
        return ResponseEntity.ok(services);
    }
}