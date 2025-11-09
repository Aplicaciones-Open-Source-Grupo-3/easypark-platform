package com.easypark.platform.parking.interfaces.rest.transform;

import com.easypark.platform.parking.domain.model.commands.CloseOperationCommand;
import com.easypark.platform.parking.interfaces.rest.resources.CloseOperationResource;

public class CloseOperationCommandFromResourceAssembler {

    public static CloseOperationCommand toCommandFromResource(
            Long operationId,
            CloseOperationResource resource,
            Long operatorId) {
        return new CloseOperationCommand(
                operationId,
                operatorId,
                resource.finalCash(),
                resource.notes()
        );
    }
}

