package com.easypark.platform.parking.interfaces.rest.resources;

public record CreateIncidentResource(
    Long vehicleId,
    String incidentType,
    String location,
    String description
) {
}

