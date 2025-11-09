package com.easypark.platform.parking.domain.services;

import com.easypark.platform.parking.domain.model.entities.Incident;

import java.util.List;

public interface IncidentQueryService {
    List<Incident> getIncidentsByBusinessId(Long businessId);
    List<Incident> getPendingIncidentsByBusinessId(Long businessId);
}

