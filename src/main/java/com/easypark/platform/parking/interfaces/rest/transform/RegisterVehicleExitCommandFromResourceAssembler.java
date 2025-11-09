package com.easypark.platform.parking.interfaces.rest.transform;

import com.easypark.platform.parking.domain.model.commands.RegisterVehicleExitCommand;
import com.easypark.platform.parking.interfaces.rest.resources.RegisterVehicleExitResource;

public class RegisterVehicleExitCommandFromResourceAssembler {

    public static RegisterVehicleExitCommand toCommandFromResource(
            Long vehicleId,
            RegisterVehicleExitResource resource,
            Long operatorId) {
        return new RegisterVehicleExitCommand(
                vehicleId,
                operatorId,
                resource.amountPaid()
        );
    }
}

