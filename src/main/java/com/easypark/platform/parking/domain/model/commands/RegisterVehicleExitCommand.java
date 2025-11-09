package com.easypark.platform.parking.domain.model.commands;

public record RegisterVehicleExitCommand(
    Long vehicleId,
    Long operatorId,
    Double amountPaid
) {
}

