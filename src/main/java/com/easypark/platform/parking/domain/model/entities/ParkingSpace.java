package com.easypark.platform.parking.domain.model.entities;

import com.easypark.platform.iam.domain.model.aggregates.Business;
import com.easypark.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "parking_space")
public class ParkingSpace extends AuditableAbstractAggregateRoot<ParkingSpace> {

    @NotBlank
    @Size(max = 20)
    @Column(name = "space_number", nullable = false)
    private String spaceNumber;

    @NotNull
    @Column(nullable = false)
    private Boolean isOccupied = false;

    @Column(name = "vehicle_type", length = 20)
    private String vehicleType;

    @Column(name = "current_vehicle_license_plate", length = 20)
    private String currentVehicleLicensePlate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @NotNull
    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(length = 200)
    private String notes;

    public ParkingSpace() {
    }

    public ParkingSpace(String spaceNumber, Business business) {
        this.spaceNumber = spaceNumber;
        this.business = business;
        this.isOccupied = false;
        this.isActive = true;
    }

    public void occupy(String vehicleType, String licensePlate) {
        this.isOccupied = true;
        this.vehicleType = vehicleType;
        this.currentVehicleLicensePlate = licensePlate;
    }

    public void release() {
        this.isOccupied = false;
        this.vehicleType = null;
        this.currentVehicleLicensePlate = null;
    }

    // Getters
    public String getSpaceNumber() {
        return spaceNumber;
    }

    public Boolean getIsOccupied() {
        return isOccupied;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getCurrentVehicleLicensePlate() {
        return currentVehicleLicensePlate;
    }

    public Business getBusiness() {
        return business;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}

