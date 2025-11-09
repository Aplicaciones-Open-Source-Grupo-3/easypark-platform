package com.easypark.platform.parking.interfaces.rest.transform;

import com.easypark.platform.parking.domain.model.commands.CreateParkingSettingsCommand;
import com.easypark.platform.parking.interfaces.rest.resources.CreateParkingSettingsResource;

public class CreateParkingSettingsCommandFromResourceAssembler {

    public static CreateParkingSettingsCommand toCommandFromResource(
            CreateParkingSettingsResource resource,
            Long businessId) {

        return new CreateParkingSettingsCommand(
                businessId,
                resource.maxCapacity(),
                resource.motorcycleRate(),
                resource.carTruckRate(),
                resource.nightRate(),
                resource.openingTime(),
                resource.closingTime(),
                resource.currency(),
                resource.gracePeriodMinutes(),
                resource.allowOvernight()
        );
    }
}

