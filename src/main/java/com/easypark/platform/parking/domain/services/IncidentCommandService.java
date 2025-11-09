package com.easypark.platform.parking.domain.services;

import com.easypark.platform.parking.domain.model.entities.Incident;
import com.easypark.platform.parking.domain.model.commands.CreateIncidentCommand;

import java.util.List;
import java.util.Optional;

public interface IncidentCommandService {
    Optional<Incident> handle(CreateIncidentCommand command);
    Optional<Incident> resolveIncident(Long incidentId, Long resolvedBy, String resolutionNotes);
}

