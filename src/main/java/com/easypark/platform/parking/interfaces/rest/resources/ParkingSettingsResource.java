package com.easypark.platform.parking.interfaces.rest.resources;

public record ParkingSettingsResource(
    String businessId,
    Integer maxCapacity,
    Double motorcycleRate,
    Double carTruckRate,
    Double nightRate,
    String openingTime,
    String closingTime,
    String currency,
    Integer gracePeriodMinutes,
    Boolean allowOvernight
) {
}

