package com.easypark.platform.parking.domain.model.aggregates;

import com.easypark.platform.iam.domain.model.aggregates.Business;
import com.easypark.platform.iam.domain.model.aggregates.User;
import com.easypark.platform.parking.domain.model.commands.RegisterVehicleEntryCommand;
import com.easypark.platform.parking.domain.model.valueobjects.VehicleStatus;
import com.easypark.platform.parking.domain.model.valueobjects.VehicleType;
import com.easypark.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
public class Vehicle extends AuditableAbstractAggregateRoot<Vehicle> {

    @Column(name = "registration_number", length = 50)
    private String registrationNumber;

    @NotBlank
    @Size(max = 20)
    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, length = 20)
    private VehicleType vehicleType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VehicleStatus status;

    @NotNull
    @Column(name = "entry_time", nullable = false)
    private Date entryTime;

    @Column(name = "exit_time")
    private Date exitTime;


    @Column(name = "parking_duration_minutes")
    private Integer parkingDurationMinutes;

    @Column(name = "amount_charged")
    private Double amountCharged;

    @Column(name = "amount_paid")
    private Double amountPaid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_entry_id", nullable = false)
    private User operatorEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_exit_id")
    private User operatorExit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id")
    private Operation operation;

    public Vehicle() {
    }

    public Vehicle(RegisterVehicleEntryCommand command, Business business, User operator, Operation operation) {
        this.licensePlate = command.licensePlate().toUpperCase();
        this.vehicleType = command.vehicleType();
        this.status = VehicleStatus.INSIDE;
        this.entryTime = new Date();
        this.business = business;
        this.operatorEntry = operator;
        this.operation = operation;
    }

    public void registerExit(User operator, Double amountPaid, Double amountCharged) {
        this.exitTime = new Date();
        this.operatorExit = operator;
        this.amountPaid = amountPaid;
        this.amountCharged = amountCharged;

        // Calcular duración en minutos
        long diffInMillis = this.exitTime.getTime() - this.entryTime.getTime();
        this.parkingDurationMinutes = (int) (diffInMillis / (1000 * 60));

        // Determinar el estado según el pago
        if (amountPaid >= amountCharged) {
            this.status = VehicleStatus.EXITED;
        } else {
            this.status = VehicleStatus.DEBT;
        }
    }

    // Getters
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public Date getExitTime() {
        return exitTime;
    }


    public Integer getParkingDurationMinutes() {
        return parkingDurationMinutes;
    }

    public Double getAmountCharged() {
        return amountCharged;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public Business getBusiness() {
        return business;
    }

    public User getOperatorEntry() {
        return operatorEntry;
    }

    public User getOperatorExit() {
        return operatorExit;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}

