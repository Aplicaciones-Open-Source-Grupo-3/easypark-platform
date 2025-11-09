package com.easypark.platform.parking.application.internal.queryservices;

import com.easypark.platform.parking.domain.model.entities.Incident;
import com.easypark.platform.parking.domain.model.valueobjects.IncidentState;
import com.easypark.platform.parking.domain.services.IncidentQueryService;
import com.easypark.platform.parking.infrastructure.persistence.jpa.repositories.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentQueryServiceImpl implements IncidentQueryService {

    private final IncidentRepository incidentRepository;

    public IncidentQueryServiceImpl(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @Override
    public List<Incident> getIncidentsByBusinessId(Long businessId) {
        return incidentRepository.findByBusinessIdOrderByReportedAtDesc(businessId);
    }

    @Override
    public List<Incident> getPendingIncidentsByBusinessId(Long businessId) {
        return incidentRepository.findByBusinessIdAndStateOrderByReportedAtDesc(businessId, IncidentState.PENDING);
    }
}


