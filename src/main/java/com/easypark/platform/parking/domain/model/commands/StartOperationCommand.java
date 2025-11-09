package com.easypark.platform.parking.domain.model.commands;

public record StartOperationCommand(
    Long businessId,
    Long operatorId,
    Double initialCash
) {
}

