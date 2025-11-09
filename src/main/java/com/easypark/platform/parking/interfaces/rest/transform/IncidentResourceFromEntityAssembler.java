package com.easypark.platform.parking.interfaces.rest.transform;

import com.easypark.platform.parking.domain.model.entities.Incident;
import com.easypark.platform.parking.interfaces.rest.resources.IncidentResource;

import java.text.SimpleDateFormat;

public class IncidentResourceFromEntityAssembler {

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public static IncidentResource toResourceFromEntity(Incident entity) {
        String state = mapState(entity.getState().name());

        return new IncidentResource(
                entity.getId().toString(),
                timeFormat.format(entity.getReportedAt()),
                entity.getLocation(),
                entity.getDescription(),
                state,
                entity.getBusiness().getId().toString()
        );
    }

    private static String mapState(String state) {
        return switch (state) {
            case "PENDING" -> "open";
            case "IN_PROGRESS" -> "in_progress";
            case "RESOLVED" -> "closed";
            case "CANCELLED" -> "closed";
            default -> state.toLowerCase();
        };
    }
}

