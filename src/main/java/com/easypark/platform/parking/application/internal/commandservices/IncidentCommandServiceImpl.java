package com.easypark.platform.parking.application.internal.commandservices;

import com.easypark.platform.iam.infrastructure.persistence.jpa.repositories.BusinessRepository;
import com.easypark.platform.parking.domain.model.entities.Incident;
import com.easypark.platform.parking.domain.model.commands.CreateIncidentCommand;
import com.easypark.platform.parking.domain.services.IncidentCommandService;
import com.easypark.platform.parking.infrastructure.persistence.jpa.repositories.IncidentRepository;
import com.easypark.platform.parking.infrastructure.persistence.jpa.repositories.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IncidentCommandServiceImpl implements IncidentCommandService {

    private final IncidentRepository incidentRepository;
    private final VehicleRepository vehicleRepository;
    private final BusinessRepository businessRepository;

    public IncidentCommandServiceImpl(IncidentRepository incidentRepository,
                                     VehicleRepository vehicleRepository,
                                     BusinessRepository businessRepository) {
        this.incidentRepository = incidentRepository;
        this.vehicleRepository = vehicleRepository;
        this.businessRepository = businessRepository;
    }

    @Override
    @Transactional
    public Optional<Incident> handle(CreateIncidentCommand command) {
        // Validar que el negocio existe
        var business = businessRepository.findById(command.businessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        // Buscar vehículo (puede ser null si el incidente no está relacionado con un vehículo)
        var vehicle = command.vehicleId() != null
                ? vehicleRepository.findById(command.vehicleId()).orElse(null)
                : null;

        // Crear incidente
        var incident = new Incident(vehicle, business, command.reportedBy(),
                                   command.incidentType(), command.location(), command.description());

        return Optional.of(incidentRepository.save(incident));
    }

    @Override
    @Transactional
    public Optional<Incident> resolveIncident(Long incidentId, Long resolvedBy, String resolutionNotes) {
        var incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        incident.resolve(resolvedBy, resolutionNotes);
        return Optional.of(incidentRepository.save(incident));
    }
}

