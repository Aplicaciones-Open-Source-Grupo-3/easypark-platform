package com.easypark.platform.clients.interfaces.rest.resources;

public record SubscriberResource(
    String id,
    String name,
    String phone,
    String email,
    String vehiclePlate,
    Integer subscriptionMonths,
    Double amount,
    String startDate,
    String paymentDate,
    String status,
    String businessId
) {
}

