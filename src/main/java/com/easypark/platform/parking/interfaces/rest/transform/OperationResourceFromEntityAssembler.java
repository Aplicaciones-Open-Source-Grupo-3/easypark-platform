package com.easypark.platform.parking.interfaces.rest.transform;

import com.easypark.platform.parking.domain.model.aggregates.Operation;
import com.easypark.platform.parking.interfaces.rest.resources.OperationResource;

import java.text.SimpleDateFormat;

public class OperationResourceFromEntityAssembler {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public static OperationResource toResourceFromEntity(Operation entity) {
        String status = entity.getIsOpen() ? "open" : "closed";

        return new OperationResource(
                entity.getId().toString(),
                dateFormat.format(entity.getOperationDate()),
                timeFormat.format(entity.getStartTime()),
                entity.getEndTime() != null ? timeFormat.format(entity.getEndTime()) : null,
                status,
                entity.getBusiness().getId().toString()
        );
    }
}

