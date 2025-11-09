package com.easypark.platform.clients.interfaces.rest.transform;

import com.easypark.platform.clients.domain.model.commands.CreateSubscriberCommand;
import com.easypark.platform.clients.interfaces.rest.resources.CreateSubscriberResource;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateSubscriberCommandFromResourceAssembler {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static CreateSubscriberCommand toCommandFromResource(
            CreateSubscriberResource resource,
            Long businessId) {

        Date startDate = null;
        Date paymentDate = null;

        try {
            if (resource.startDate() != null) {
                startDate = dateFormat.parse(resource.startDate());
            }
            if (resource.paymentDate() != null) {
                paymentDate = dateFormat.parse(resource.paymentDate());
            }
        } catch (Exception e) {
            // Handle parsing error
        }

        return new CreateSubscriberCommand(
                resource.name(),
                resource.email(),
                resource.phone(),
                resource.vehiclePlate(),
                resource.subscriptionMonths(),
                resource.amount(),
                startDate,
                paymentDate,
                businessId
        );
    }
}

