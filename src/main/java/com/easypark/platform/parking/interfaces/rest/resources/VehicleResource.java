package com.easypark.platform.parking.interfaces.rest.resources;

public record VehicleResource(
    String id,
    String registrationNumber,
    String entryDate,
    String vehicleType,
    String plate,
    String entryTime,
    String status,
    String exitDate,
    String exitTime,
    String businessId
) {
}

