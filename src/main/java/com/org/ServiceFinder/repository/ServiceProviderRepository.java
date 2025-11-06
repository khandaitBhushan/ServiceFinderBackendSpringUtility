package com.org.ServiceFinder.repository;

import com.org.ServiceFinder.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {

    // FIXED: Flexible search with LIKE and case-insensitive
    @Query("SELECT sp FROM ServiceProvider sp WHERE " +
            "(:category IS NULL OR LOWER(sp.serviceCategory.name) LIKE LOWER(CONCAT('%', :category, '%'))) AND " +
            "(:location IS NULL OR LOWER(sp.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    List<ServiceProvider> searchProviders(
            @Param("category") String category,
            @Param("location") String location);

    // ADD THIS METHOD: For category-only searches
    @Query("SELECT sp FROM ServiceProvider sp WHERE " +
            "(:category IS NULL OR LOWER(sp.serviceCategory.name) LIKE LOWER(CONCAT('%', :category, '%')))")
    List<ServiceProvider> findByServiceCategoryNameContaining(@Param("category") String category);

    // ADD THIS METHOD: For radius searches by category
    List<ServiceProvider> findByServiceCategoryName(String categoryName);

    // Alternative method naming approach
    List<ServiceProvider> findByServiceCategoryNameContainingIgnoreCaseAndLocationContainingIgnoreCase(
            String category, String location);
}