package com.easypark.platform.clients.interfaces.rest.resources;

public record UpdateSubscriberResource(
    String fullName,
    String email,
    String phone,
    String address,
    String vehicleLicensePlate,
    String vehicleType
) {
}

