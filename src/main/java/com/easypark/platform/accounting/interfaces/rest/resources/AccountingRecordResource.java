package com.easypark.platform.accounting.interfaces.rest.resources;

public record AccountingRecordResource(
    String id,
    String registrationNumber,
    String entryDate,
    String exitDate,
    String vehicleType,
    String plate,
    String entryTime,
    String exitTime,
    Double amountPaid,
    String currency,
    Double hoursParked,
    Double hoursToPay,
    Double ratePerHour,
    String operationDate,
    Double nightCharge,
    String businessId
) {
}

