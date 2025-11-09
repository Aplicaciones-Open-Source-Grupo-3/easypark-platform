package com.easypark.platform.clients.interfaces.rest.resources;

public record CreateSubscriberResource(
    String name,
    String email,
    String phone,
    String vehiclePlate,
    Integer subscriptionMonths,
    Double amount,
    String startDate,
    String paymentDate
) {
}
