package com.easypark.platform.parking.interfaces.rest.resources;

public record OperationResource(
    String id,
    String date,
    String openTime,
    String closeTime,
    String status,
    String businessId
) {
}

