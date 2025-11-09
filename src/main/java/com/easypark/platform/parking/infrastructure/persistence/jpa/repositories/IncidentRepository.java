package com.easypark.platform.parking.infrastructure.persistence.jpa.repositories;

import com.easypark.platform.parking.domain.model.entities.Incident;
import com.easypark.platform.parking.domain.model.valueobjects.IncidentState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByBusinessIdOrderByReportedAtDesc(Long businessId);
    List<Incident> findByBusinessIdAndStateOrderByReportedAtDesc(Long businessId, IncidentState state);
    List<Incident> findByVehicleId(Long vehicleId);
}

