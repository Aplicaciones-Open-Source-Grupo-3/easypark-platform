package com.easypark.platform.iam.interfaces.rest.transform;

import com.easypark.platform.iam.domain.model.commands.SignUpCommand;
import com.easypark.platform.iam.interfaces.rest.resources.SignUpResource;

public class SignUpCommandFromResourceAssembler {

    public static SignUpCommand toCommandFromResource(SignUpResource resource) {
        return new SignUpCommand(
            resource.businessName(),
            resource.address(),
            resource.phone(),
            resource.email(),
            resource.taxId(),
            resource.maxCapacity(),
            resource.motorcycleRate(),
            resource.carTruckRate(),
            resource.nightRate(),
            resource.openingTime(),
            resource.closingTime(),
            resource.currency(),
            resource.adminName(),
            resource.adminUsername(),
            resource.adminEmail(),
            resource.adminPassword(),
            "ROLE_ADMIN"
        );
    }
}

