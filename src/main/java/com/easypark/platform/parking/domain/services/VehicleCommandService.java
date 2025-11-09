package com.easypark.platform.parking.domain.services;

import com.easypark.platform.parking.domain.model.aggregates.Vehicle;
import com.easypark.platform.parking.domain.model.commands.RegisterVehicleEntryCommand;
import com.easypark.platform.parking.domain.model.commands.RegisterVehicleExitCommand;

import java.util.Optional;

public interface VehicleCommandService {
    Optional<Vehicle> handle(RegisterVehicleEntryCommand command);
    Optional<Vehicle> handle(RegisterVehicleExitCommand command);
    boolean deleteVehicle(Long vehicleId, Long businessId);
}
