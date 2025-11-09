package com.easypark.platform.analytics.interfaces.rest.resources;

public record OccupancyRateResource(
    Integer totalSpaces,
    Integer occupiedSpaces,
    Integer availableSpaces,
    Double occupancyPercentage
) {
}

