package com.easypark.platform.clients.interfaces.rest.transform;

import com.easypark.platform.clients.domain.model.aggregates.Subscriber;
import com.easypark.platform.clients.interfaces.rest.resources.SubscriberResource;

import java.text.SimpleDateFormat;

public class SubscriberResourceFromEntityAssembler {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static SubscriberResource toResourceFromEntity(Subscriber entity) {
        return new SubscriberResource(
                entity.getId().toString(),
                entity.getFullName(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getVehicleLicensePlate(),
                entity.getSubscriptionMonths(),
                entity.getAmount(),
                entity.getStartDate() != null ? dateFormat.format(entity.getStartDate()) : null,
                entity.getPaymentDate() != null ? dateFormat.format(entity.getPaymentDate()) : null,
                entity.getStatus(),
                entity.getBusiness().getId().toString()
        );
    }
}

