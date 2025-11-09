package com.easypark.platform.parking.interfaces.rest.resources;

import com.easypark.platform.parking.domain.model.valueobjects.VehicleType;

public record RegisterVehicleEntryResource(
    String licensePlate,
    VehicleType vehicleType
) {
}
