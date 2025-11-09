package com.easypark.platform.parking.infrastructure.persistence.jpa.repositories;

import com.easypark.platform.parking.domain.model.aggregates.Vehicle;
import com.easypark.platform.parking.domain.model.valueobjects.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByBusinessId(Long businessId);
    List<Vehicle> findByBusinessIdAndStatus(Long businessId, VehicleStatus status);
    Optional<Vehicle> findByIdAndBusinessId(Long id, Long businessId);
    List<Vehicle> findByOperationId(Long operationId);
    long countByBusinessId(Long businessId);
}
