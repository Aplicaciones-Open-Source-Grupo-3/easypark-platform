package com.easypark.platform.parking.interfaces.rest.resources;

public record CreateParkingSettingsResource(
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
    // Constructor con valores por defecto
    public CreateParkingSettingsResource {
        if (gracePeriodMinutes == null) {
            gracePeriodMinutes = 15;
        }
        if (allowOvernight == null) {
            allowOvernight = true;
        }
        if (currency == null || currency.isBlank()) {
            currency = "USD";
        }
    }
}

