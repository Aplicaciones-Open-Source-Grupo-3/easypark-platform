package com.easypark.platform.parking.interfaces.rest.transform;

import com.easypark.platform.parking.domain.model.commands.RegisterVehicleEntryCommand;
import com.easypark.platform.parking.interfaces.rest.resources.RegisterVehicleEntryResource;

public class RegisterVehicleEntryCommandFromResourceAssembler {

    public static RegisterVehicleEntryCommand toCommandFromResource(
            RegisterVehicleEntryResource resource,
            Long businessId,
            Long operatorId) {
        return new RegisterVehicleEntryCommand(
                resource.licensePlate(),
                resource.vehicleType(),
                businessId,
                operatorId
        );
    }
}
