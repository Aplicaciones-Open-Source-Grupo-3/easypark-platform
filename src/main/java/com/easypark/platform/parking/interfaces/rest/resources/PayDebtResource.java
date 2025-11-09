package com.easypark.platform.parking.interfaces.rest.resources;

public record PayDebtResource(
    Double paymentAmount,
    String notes
) {
}
