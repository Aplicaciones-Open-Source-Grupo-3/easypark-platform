package com.easypark.platform.parking.infrastructure.persistence.jpa.repositories;

import com.easypark.platform.parking.domain.model.entities.VehicleDebt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleDebtRepository extends JpaRepository<VehicleDebt, Long> {
    List<VehicleDebt> findByBusinessIdAndIsPaidFalseOrderByDebtDateDesc(Long businessId);
    List<VehicleDebt> findByBusinessIdOrderByDebtDateDesc(Long businessId);
    List<VehicleDebt> findByVehicleId(Long vehicleId);
}

