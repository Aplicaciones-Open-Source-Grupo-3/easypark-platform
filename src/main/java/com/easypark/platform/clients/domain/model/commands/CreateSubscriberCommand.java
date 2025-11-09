package com.easypark.platform.clients.domain.model.commands;

public record CreateSubscriberCommand(
    String fullName,
    String email,
    String phone,
    String vehicleLicensePlate,
    Integer subscriptionMonths,
    Double amount,
    java.util.Date startDate,
    java.util.Date paymentDate,
    Long businessId
) {
}
