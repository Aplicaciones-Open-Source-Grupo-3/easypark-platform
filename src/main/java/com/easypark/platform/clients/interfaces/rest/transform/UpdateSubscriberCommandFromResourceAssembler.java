package com.easypark.platform.clients.interfaces.rest.transform;

import com.easypark.platform.clients.domain.model.commands.UpdateSubscriberCommand;
import com.easypark.platform.clients.interfaces.rest.resources.UpdateSubscriberResource;

public class UpdateSubscriberCommandFromResourceAssembler {

    public static UpdateSubscriberCommand toCommandFromResource(
            Long subscriberId,
            UpdateSubscriberResource resource) {
        return new UpdateSubscriberCommand(
                subscriberId,
                resource.fullName(),
                resource.email(),
                resource.phone(),
                resource.address(),
                resource.vehicleLicensePlate(),
                resource.vehicleType()
        );
    }
}

