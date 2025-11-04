package com.easypark.platform.iam.domain.model.commands;

public record SignUpCommand(
    String businessName,
    String address,
    String phone,
    String email,
    String taxId,
    Integer maxCapacity,
    Double motorcycleRate,
    Double carTruckRate,
    Double nightRate,
    String openingTime,
    String closingTime,
    String currency,
    String adminName,
    String adminUsername,
    String adminEmail,
    String adminPassword,
    String role
) {
}

