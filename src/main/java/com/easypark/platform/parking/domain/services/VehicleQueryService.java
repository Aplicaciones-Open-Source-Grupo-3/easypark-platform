package com.easypark.platform.parking.domain.services;

import com.easypark.platform.parking.domain.model.aggregates.Vehicle;
import com.easypark.platform.parking.domain.model.queries.GetAllVehiclesByBusinessIdQuery;
import com.easypark.platform.parking.domain.model.queries.GetVehicleByIdQuery;

import java.util.List;
import java.util.Optional;

public interface VehicleQueryService {
    Optional<Vehicle> handle(GetVehicleByIdQuery query);
    List<Vehicle> handle(GetAllVehiclesByBusinessIdQuery query);
    List<Vehicle> getVehiclesInside(Long businessId);
}

