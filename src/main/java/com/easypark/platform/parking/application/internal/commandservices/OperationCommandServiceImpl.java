package com.easypark.platform.parking.application.internal.commandservices;

import com.easypark.platform.iam.infrastructure.persistence.jpa.repositories.BusinessRepository;
import com.easypark.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.easypark.platform.parking.domain.model.aggregates.Operation;
import com.easypark.platform.parking.domain.model.commands.CloseOperationCommand;
import com.easypark.platform.parking.domain.model.commands.StartOperationCommand;
import com.easypark.platform.parking.domain.services.OperationCommandService;
import com.easypark.platform.parking.infrastructure.persistence.jpa.repositories.OperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OperationCommandServiceImpl implements OperationCommandService {

    private final OperationRepository operationRepository;
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;

    public OperationCommandServiceImpl(OperationRepository operationRepository,
                                      BusinessRepository businessRepository,
                                      UserRepository userRepository) {
        this.operationRepository = operationRepository;
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Optional<Operation> handle(StartOperationCommand command) {
        // Validar que el negocio existe
        var business = businessRepository.findById(command.businessId())
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        // Validar que el operador existe
        var operator = userRepository.findById(command.operatorId())
                .orElseThrow(() -> new RuntimeException("Operador no encontrado"));

        // Verificar que no haya una operación abierta
        var openOperation = operationRepository.findOpenOperationByBusinessId(command.businessId());
        if (openOperation.isPresent()) {
            throw new RuntimeException("Ya existe una operación abierta. Por favor ciérrela primero.");
        }

        // Crear nueva operación (sin restricción de una por día)
        var operation = new Operation(command, business, operator);
        return Optional.of(operationRepository.save(operation));
    }

    @Override
    @Transactional
    public Optional<Operation> handle(CloseOperationCommand command) {
        // Buscar operación
        var operation = operationRepository.findById(command.operationId())
                .orElseThrow(() -> new RuntimeException("Operación no encontrada"));

        // Validar que la operación está abierta
        if (!operation.getIsOpen()) {
            throw new RuntimeException("La operación ya está cerrada");
        }

        // Validar que el operador existe
        var operator = userRepository.findById(command.operatorId())
                .orElseThrow(() -> new RuntimeException("Operador no encontrado"));

        // Cerrar operación
        operation.close(operator, command.finalCash(), command.notes());
        return Optional.of(operationRepository.save(operation));
    }
}
