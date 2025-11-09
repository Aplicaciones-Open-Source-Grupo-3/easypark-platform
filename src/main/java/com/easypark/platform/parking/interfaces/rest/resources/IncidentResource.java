package com.easypark.platform.parking.interfaces.rest.resources;

public record IncidentResource(
    String id,
    String time,
    String location,
    String description,
    String state,
    String businessId
) {
}

