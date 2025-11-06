package com.org.ServiceFinder.repository;

import com.org.ServiceFinder.model.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
    Optional<ServiceCategory> findByName(String name);
}