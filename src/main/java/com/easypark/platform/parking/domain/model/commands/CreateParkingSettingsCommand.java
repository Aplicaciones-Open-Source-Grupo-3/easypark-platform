package com.easypark.platform.parking.domain.model.commands;

public record CreateParkingSettingsCommand(
    Long businessId,
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

