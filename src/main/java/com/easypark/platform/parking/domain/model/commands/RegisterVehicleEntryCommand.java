package com.easypark.platform.parking.domain.model.commands;

import com.easypark.platform.parking.domain.model.valueobjects.VehicleType;

public record RegisterVehicleEntryCommand(
    String licensePlate,
    VehicleType vehicleType,
    Long businessId,
    Long operatorId
) {
}
