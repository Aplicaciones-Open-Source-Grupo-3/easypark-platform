package com.easypark.platform.parking.application.internal.commandservices;

import com.easypark.platform.iam.domain.model.aggregates.Business;
import com.easypark.platform.iam.infrastructure.persistence.jpa.repositories.BusinessRepository;
import com.easypark.platform.parking.domain.model.commands.CreateParkingSettingsCommand;
import com.easypark.platform.parking.domain.services.ParkingSettingsCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ParkingSettingsCommandServiceImpl implements ParkingSettingsCommandService {

    private final BusinessRepository businessRepository;

    public ParkingSettingsCommandServiceImpl(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    @Transactional
    public Optional<Business> handle(CreateParkingSettingsCommand command) {
        // Buscar el negocio
        var business = businessRepository.findById(command.businessId())
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        // Actualizar configuraciones de parking
        business.updateParkingSettings(
                command.maxCapacity(),
                command.motorcycleRate(),
                command.carTruckRate(),
                command.nightRate(),
                command.openingTime(),
                command.closingTime(),
                command.currency(),
                command.gracePeriodMinutes(),
                command.allowOvernight()
        );

        // Guardar cambios
        var updatedBusiness = businessRepository.save(business);
        return Optional.of(updatedBusiness);
    }
}

