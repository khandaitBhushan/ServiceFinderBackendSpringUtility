package com.org.ServiceFinder.repository;

import com.org.ServiceFinder.model.ProviderService;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProviderServiceRepository extends JpaRepository<ProviderService, Long> {
    List<ProviderService> findByProviderId(Long providerId);
    List<ProviderService> findByProviderIdAndIsActiveTrue(Long providerId);
    List<ProviderService> findByServiceNameContainingIgnoreCaseAndIsActiveTrue(String serviceName);
}