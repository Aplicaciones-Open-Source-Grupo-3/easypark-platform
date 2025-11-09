package com.easypark.platform.parking.interfaces.rest.resources;

public record CloseOperationResource(
    Double finalCash,
    String notes
) {
}

