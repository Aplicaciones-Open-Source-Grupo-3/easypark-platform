package com.easypark.platform.parking.interfaces.rest.transform;

import com.easypark.platform.iam.domain.model.aggregates.Business;
import com.easypark.platform.parking.interfaces.rest.resources.ParkingSettingsResource;

public class ParkingSettingsResourceFromEntityAssembler {

    public static ParkingSettingsResource toResourceFromEntity(Business business) {
        return new ParkingSettingsResource(
                business.getId().toString(),
                business.getMaxCapacity(),
                business.getMotorcycleRate(),
                business.getCarTruckRate(),
                business.getNightRate(),
                business.getOpeningTime(),
                business.getClosingTime(),
                business.getCurrency(),
                business.getGracePeriodMinutes(),
                business.getAllowOvernight()
        );
    }
}

