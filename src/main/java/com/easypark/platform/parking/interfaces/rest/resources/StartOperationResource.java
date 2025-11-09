package com.easypark.platform.parking.interfaces.rest.resources;

public record StartOperationResource(
    Double initialCash
) {
    // Constructor con valores por defecto
    public StartOperationResource {
        if (initialCash == null) {
            initialCash = 0.0;
        }
    }
}

