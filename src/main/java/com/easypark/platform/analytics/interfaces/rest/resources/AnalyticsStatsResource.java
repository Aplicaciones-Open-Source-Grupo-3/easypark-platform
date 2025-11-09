package com.easypark.platform.analytics.interfaces.rest.resources;

public record AnalyticsStatsResource(
    Long totalVehiclesToday,
    Long totalVehiclesInside,
    Double todayRevenue,
    Double monthRevenue,
    Integer totalSubscribers,
    Long totalIncidents,
    Long pendingIncidents
) {
}

