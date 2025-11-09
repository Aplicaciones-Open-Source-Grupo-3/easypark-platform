package com.easypark.platform.parking.interfaces.rest.resources;

public record VehicleDebtResource(
    String id,
    String vehicleId,
    String plate,
    String vehicleType,
    String entryDate,
    String entryTime,
    Double regularHours,
    Double regularAmount,
    Double nightCharge,
    Double totalDebt,
    Boolean isPaid,
    String lastUpdated,
    String businessId
) {
}

