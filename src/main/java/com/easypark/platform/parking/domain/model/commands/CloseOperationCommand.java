package com.easypark.platform.parking.domain.model.commands;

public record CloseOperationCommand(
    Long operationId,
    Long operatorId,
    Double finalCash,
    String notes
) {
}

