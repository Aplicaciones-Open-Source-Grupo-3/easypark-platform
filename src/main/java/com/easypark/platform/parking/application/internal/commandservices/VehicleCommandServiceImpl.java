package com.easypark.platform.parking.application.internal.commandservices;

import com.easypark.platform.accounting.domain.model.commands.CreateAccountingRecordCommand;
import com.easypark.platform.accounting.domain.services.AccountingCommandService;
import com.easypark.platform.accounting.domain.model.valueobjects.RecordType;
import com.easypark.platform.accounting.infrastructure.persistence.jpa.repositories.AccountingRecordRepository;
import com.easypark.platform.iam.infrastructure.persistence.jpa.repositories.BusinessRepository;
import com.easypark.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.easypark.platform.parking.domain.model.aggregates.Vehicle;
import com.easypark.platform.parking.domain.model.commands.RegisterVehicleEntryCommand;
import com.easypark.platform.parking.domain.model.commands.RegisterVehicleExitCommand;
import com.easypark.platform.parking.domain.model.entities.VehicleDebt;
import com.easypark.platform.parking.domain.model.valueobjects.VehicleType;
import com.easypark.platform.parking.domain.services.VehicleCommandService;
import com.easypark.platform.parking.infrastructure.persistence.jpa.repositories.OperationRepository;
import com.easypark.platform.parking.infrastructure.persistence.jpa.repositories.VehicleDebtRepository;
import com.easypark.platform.parking.infrastructure.persistence.jpa.repositories.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class VehicleCommandServiceImpl implements VehicleCommandService {

    private final VehicleRepository vehicleRepository;
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final OperationRepository operationRepository;
    private final VehicleDebtRepository vehicleDebtRepository;
    private final AccountingCommandService accountingCommandService;
    private final AccountingRecordRepository accountingRecordRepository;

    public VehicleCommandServiceImpl(VehicleRepository vehicleRepository,
                                    BusinessRepository businessRepository,
                                    UserRepository userRepository,
                                    OperationRepository operationRepository,
                                    VehicleDebtRepository vehicleDebtRepository,
                                    AccountingCommandService accountingCommandService,
                                    AccountingRecordRepository accountingRecordRepository) {
        this.vehicleRepository = vehicleRepository;
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
        this.operationRepository = operationRepository;
        this.vehicleDebtRepository = vehicleDebtRepository;
        this.accountingCommandService = accountingCommandService;
        this.accountingRecordRepository = accountingRecordRepository;
    }

    @Override
    @Transactional
    public Optional<Vehicle> handle(RegisterVehicleEntryCommand command) {
        // Validar que el negocio existe
        var business = businessRepository.findById(command.businessId())
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        // Validar que el operador existe
        var operator = userRepository.findById(command.operatorId())
                .orElseThrow(() -> new RuntimeException("Operador no encontrado"));

        // Buscar operación abierta - OBLIGATORIO
        var operation = operationRepository.findOpenOperationByBusinessId(command.businessId())
                .orElseThrow(() -> new RuntimeException("Debe iniciar operaciones antes de registrar vehículos. Por favor, inicie las operaciones del día primero."));

        // Crear vehículo
        var vehicle = new Vehicle(command, business, operator, operation);

        // Generar registration number automático
        long count = vehicleRepository.countByBusinessId(command.businessId());
        String registrationNumber = String.format("%04d", count + 1);
        vehicle.setRegistrationNumber(registrationNumber);

        var savedVehicle = vehicleRepository.save(vehicle);

        // Incrementar contador en la operación
        operation.incrementVehicleCount(command.vehicleType().name());
        operationRepository.save(operation);

        return Optional.of(savedVehicle);
    }

    @Override
    @Transactional
    public Optional<Vehicle> handle(RegisterVehicleExitCommand command) {
        // Buscar vehículo
        var vehicle = vehicleRepository.findById(command.vehicleId())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        // Validar que el operador existe
        var operator = userRepository.findById(command.operatorId())
                .orElseThrow(() -> new RuntimeException("Operador no encontrado"));

        // Calcular monto a cobrar
        var business = vehicle.getBusiness();
        var feeCalculation = calculateParkingFeeWithDetails(vehicle, business);
        var amountCharged = feeCalculation[0]; // total amount
        var regularHours = feeCalculation[1];
        var regularAmount = feeCalculation[2];
        var nightCharge = feeCalculation[3];

        // Registrar salida
        vehicle.registerExit(operator, command.amountPaid(), amountCharged);
        var savedVehicle = vehicleRepository.save(vehicle);

        // Si hay deuda, crear registro de deuda
        if (command.amountPaid() < amountCharged) {
            var debtAmount = amountCharged - command.amountPaid();
            var debt = new VehicleDebt(vehicle, business, regularHours, regularAmount,
                                       nightCharge, debtAmount, command.amountPaid());
            vehicleDebtRepository.save(debt);
        }

        // Agregar ingreso a la operación
        if (command.amountPaid() > 0) {
            var operation = vehicle.getOperation();
            operation.addIncome(command.amountPaid());
            operationRepository.save(operation);
        }

        // ✅ CREAR REGISTRO EN ACCOUNTING AUTOMÁTICAMENTE
        createAccountingRecordFromVehicleExit(vehicle, regularHours, regularAmount, nightCharge, command.operatorId());

        return Optional.of(savedVehicle);
    }

    /**
     * Crea un registro contable automáticamente cuando un vehículo sale del estacionamiento.
     * Todos los datos del vehículo se copian al registro de accounting.
     */
    private void createAccountingRecordFromVehicleExit(Vehicle vehicle, Double hoursParked,
                                                       Double regularAmount, Double nightCharge,
                                                       Long operatorId) {
        try {
            // Calcular tarifa por hora según el tipo de vehículo
            Double ratePerHour = vehicle.getVehicleType() == VehicleType.MOTORCYCLE
                ? vehicle.getBusiness().getMotorcycleRate()
                : vehicle.getBusiness().getCarTruckRate();

            // Crear comando básico para accounting
            var accountingCommand = new CreateAccountingRecordCommand(
                vehicle.getBusiness().getId(),
                RecordType.INCOME,
                vehicle.getAmountPaid(),
                "Pago de estacionamiento - Placa: " + vehicle.getLicensePlate(),
                "Parking",
                new Date(),
                vehicle.getOperation() != null ? vehicle.getOperation().getId() : null,
                operatorId
            );

            // Crear el registro contable
            var accountingRecordOpt = accountingCommandService.handle(accountingCommand);

            if (accountingRecordOpt.isPresent()) {
                var accountingRecord = accountingRecordOpt.get();

                // ✅ Copiar TODOS los datos del vehículo al registro contable
                accountingRecord.setRegistrationNumber(vehicle.getRegistrationNumber());
                accountingRecord.setEntryDate(vehicle.getEntryTime());
                accountingRecord.setExitDate(vehicle.getExitTime());
                accountingRecord.setVehicleType(vehicle.getVehicleType().name());
                accountingRecord.setPlate(vehicle.getLicensePlate());
                accountingRecord.setEntryTime(vehicle.getEntryTime());
                accountingRecord.setExitTime(vehicle.getExitTime());
                accountingRecord.setAmountPaid(vehicle.getAmountPaid());
                accountingRecord.setCurrency(vehicle.getBusiness().getCurrency());
                accountingRecord.setHoursParked(hoursParked);
                accountingRecord.setHoursToPay(Math.ceil(hoursParked)); // Redondear hacia arriba
                accountingRecord.setRatePerHour(ratePerHour);
                accountingRecord.setOperationDate(vehicle.getEntryTime());
                accountingRecord.setNightCharge(nightCharge);

                // ✅ Guardar nuevamente el registro con todos los campos actualizados del vehículo
                accountingRecordRepository.save(accountingRecord);

                System.out.println("✅ Registro contable creado automáticamente para vehículo: " + vehicle.getLicensePlate());
            }

        } catch (Exception e) {
            // Log error pero no fallar la transacción de salida del vehículo
            System.err.println("⚠️ Error al crear registro contable: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public boolean deleteVehicle(Long vehicleId, Long businessId) {
        var vehicle = vehicleRepository.findByIdAndBusinessId(vehicleId, businessId);
        if (vehicle.isPresent()) {
            vehicleRepository.delete(vehicle.get());
            return true;
        }
        return false;
    }

    private Double calculateParkingFee(Vehicle vehicle, com.easypark.platform.iam.domain.model.aggregates.Business business) {
        var details = calculateParkingFeeWithDetails(vehicle, business);
        return details[0]; // total amount
    }

    private Double[] calculateParkingFeeWithDetails(Vehicle vehicle, com.easypark.platform.iam.domain.model.aggregates.Business business) {
        long diffInMillis = new Date().getTime() - vehicle.getEntryTime().getTime();
        double totalHours = diffInMillis / (1000.0 * 60 * 60);

        if (totalHours < 1) totalHours = 1; // Mínimo 1 hora

        Double ratePerHour;
        if (vehicle.getVehicleType() == VehicleType.MOTORCYCLE) {
            ratePerHour = business.getMotorcycleRate();
        } else {
            ratePerHour = business.getCarTruckRate();
        }

        double regularAmount = totalHours * ratePerHour;
        double nightCharge = 0.0;

        // Calcular cargo nocturno si aplica (simplificado)
        // TODO: Implementar lógica más compleja basada en horarios de apertura/cierre

        double totalAmount = regularAmount + nightCharge;

        // Retorna: [totalAmount, regularHours, regularAmount, nightCharge]
        return new Double[]{totalAmount, totalHours, regularAmount, nightCharge};
    }
}

