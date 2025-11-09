package com.easypark.platform.parking.interfaces.rest.transform;

import com.easypark.platform.parking.domain.model.aggregates.Vehicle;
import com.easypark.platform.parking.interfaces.rest.resources.VehicleResource;

import java.text.SimpleDateFormat;

public class VehicleResourceFromEntityAssembler {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public static VehicleResource toResourceFromEntity(Vehicle entity) {
        String vehicleType = mapVehicleType(entity.getVehicleType().name());
        String status = mapStatus(entity.getStatus().name());

        return new VehicleResource(
                entity.getId().toString(),
                entity.getRegistrationNumber(),
                dateFormat.format(entity.getEntryTime()),
                vehicleType,
                entity.getLicensePlate(),
                timeFormat.format(entity.getEntryTime()),
                status,
                entity.getExitTime() != null ? dateFormat.format(entity.getExitTime()) : null,
                entity.getExitTime() != null ? timeFormat.format(entity.getExitTime()) : null,
                entity.getBusiness().getId().toString()
        );
    }

    private static String mapVehicleType(String type) {
        return switch (type) {
            case "MOTORCYCLE" -> "moto";
            case "CAR", "TRUCK" -> "auto-camioneta";
            default -> type.toLowerCase();
        };
    }

    private static String mapStatus(String status) {
        return switch (status) {
            case "INSIDE" -> "in-space";
            case "EXITED" -> "out";
            case "DEBT" -> "out";
            default -> status.toLowerCase();
        };
    }
}

