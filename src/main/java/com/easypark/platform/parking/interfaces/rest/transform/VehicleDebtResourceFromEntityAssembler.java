package com.easypark.platform.parking.interfaces.rest.transform;

import com.easypark.platform.parking.domain.model.entities.VehicleDebt;
import com.easypark.platform.parking.interfaces.rest.resources.VehicleDebtResource;

import java.text.SimpleDateFormat;

public class VehicleDebtResourceFromEntityAssembler {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static VehicleDebtResource toResourceFromEntity(VehicleDebt entity) {
        String vehicleType = entity.getVehicle() != null ? mapVehicleType(entity.getVehicle().getVehicleType().name()) : null;

        return new VehicleDebtResource(
                entity.getId().toString(),
                entity.getVehicle() != null ? entity.getVehicle().getId().toString() : null,
                entity.getVehicle() != null ? entity.getVehicle().getLicensePlate() : null,
                vehicleType,
                entity.getVehicle() != null ? dateFormat.format(entity.getVehicle().getEntryTime()) : null,
                entity.getVehicle() != null ? timeFormat.format(entity.getVehicle().getEntryTime()) : null,
                entity.getRegularHours(),
                entity.getRegularAmount(),
                entity.getNightCharge(),
                entity.getDebtAmount(),
                entity.getIsPaid(),
                isoFormat.format(entity.getUpdatedAt() != null ? entity.getUpdatedAt() : entity.getCreatedAt()),
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
}

