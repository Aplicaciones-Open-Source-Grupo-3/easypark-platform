package com.easypark.platform.parking.application.internal.queryservices;

import com.easypark.platform.parking.domain.model.aggregates.Vehicle;
import com.easypark.platform.parking.domain.model.queries.GetAllVehiclesByBusinessIdQuery;
import com.easypark.platform.parking.domain.model.queries.GetVehicleByIdQuery;
import com.easypark.platform.parking.domain.model.valueobjects.VehicleStatus;
import com.easypark.platform.parking.domain.services.VehicleQueryService;
import com.easypark.platform.parking.infrastructure.persistence.jpa.repositories.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleQueryServiceImpl implements VehicleQueryService {

    private final VehicleRepository vehicleRepository;

    public VehicleQueryServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Optional<Vehicle> handle(GetVehicleByIdQuery query) {
        return vehicleRepository.findById(query.vehicleId());
    }

    @Override
    public List<Vehicle> handle(GetAllVehiclesByBusinessIdQuery query) {
        return vehicleRepository.findByBusinessId(query.businessId());
    }

    @Override
    public List<Vehicle> getVehiclesInside(Long businessId) {
        return vehicleRepository.findByBusinessIdAndStatus(businessId, VehicleStatus.INSIDE);
    }
}
