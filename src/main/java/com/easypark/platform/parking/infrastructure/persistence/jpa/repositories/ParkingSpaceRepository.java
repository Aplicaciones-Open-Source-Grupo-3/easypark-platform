package com.easypark.platform.parking.infrastructure.persistence.jpa.repositories;

import com.easypark.platform.parking.domain.model.entities.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {
    List<ParkingSpace> findByBusinessId(Long businessId);
    List<ParkingSpace> findByBusinessIdAndIsOccupiedFalse(Long businessId);
    Optional<ParkingSpace> findByBusinessIdAndSpaceNumber(Long businessId, String spaceNumber);
}

