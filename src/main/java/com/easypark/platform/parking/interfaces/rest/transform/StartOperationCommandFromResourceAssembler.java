package com.easypark.platform.parking.interfaces.rest.transform;

import com.easypark.platform.parking.domain.model.commands.StartOperationCommand;
import com.easypark.platform.parking.interfaces.rest.resources.StartOperationResource;

public class StartOperationCommandFromResourceAssembler {

    public static StartOperationCommand toCommandFromResource(
            StartOperationResource resource,
            Long businessId,
            Long operatorId) {
        return new StartOperationCommand(
                businessId,
                operatorId,
                resource.initialCash()
        );
    }
}

