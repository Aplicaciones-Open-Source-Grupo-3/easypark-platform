package com.easypark.platform.iam.infrastructure.persistence.jpa.repositories;

import com.easypark.platform.iam.domain.model.aggregates.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findByEmail(String email);
    Optional<Business> findByTaxId(String taxId);
    boolean existsByEmail(String email);
    boolean existsByTaxId(String taxId);
}

