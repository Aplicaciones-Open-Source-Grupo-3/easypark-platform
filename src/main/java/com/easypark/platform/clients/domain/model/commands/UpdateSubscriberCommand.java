package com.easypark.platform.clients.domain.model.commands;

public record UpdateSubscriberCommand(
    Long subscriberId,
    String fullName,
    String email,
    String phone,
    String address,
    String vehicleLicensePlate,
    String vehicleType
) {
}

