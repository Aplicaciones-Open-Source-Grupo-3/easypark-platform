package com.easypark.platform.parking.domain.model.commands;

public record CreateIncidentCommand(
    Long vehicleId,
    Long businessId,
    Long reportedBy,
    String location,
    String description,
    String incidentType
) {
}

