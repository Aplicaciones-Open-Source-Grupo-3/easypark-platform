package com.easypark.platform.parking.interfaces.rest.transform;

import com.easypark.platform.parking.domain.model.commands.CreateIncidentCommand;
import com.easypark.platform.parking.interfaces.rest.resources.CreateIncidentResource;

public class CreateIncidentCommandFromResourceAssembler {

    public static CreateIncidentCommand toCommandFromResource(
            CreateIncidentResource resource,
            Long businessId,
            Long reportedBy) {
        return new CreateIncidentCommand(
                resource.vehicleId(),
                businessId,
                reportedBy,
                resource.location(),
                resource.description(),
                resource.incidentType()
        );
    }
}

