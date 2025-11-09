package com.easypark.platform.parking.domain.model.aggregates;

import com.easypark.platform.iam.domain.model.aggregates.Business;
import com.easypark.platform.iam.domain.model.aggregates.User;
import com.easypark.platform.parking.domain.model.commands.StartOperationCommand;
import com.easypark.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
public class Operation extends AuditableAbstractAggregateRoot<Operation> {

    @NotNull
    @Column(name = "operation_date", nullable = false)
    private Date operationDate;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @NotNull
    @Column(name = "initial_cash", nullable = false)
    private Double initialCash;

    @Column(name = "final_cash")
    private Double finalCash;

    @Column(name = "total_income")
    private Double totalIncome;

    @Column(name = "total_vehicles")
    private Integer totalVehicles = 0;

    @Column(name = "total_motorcycles")
    private Integer totalMotorcycles = 0;

    @Column(name = "total_cars")
    private Integer totalCars = 0;

    @Column(name = "total_trucks")
    private Integer totalTrucks = 0;

    @Column(nullable = false)
    private Boolean isOpen = true;

    @Column(length = 500)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opened_by", nullable = false)
    private User openedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "closed_by")
    private User closedBy;

    public Operation() {
    }

    public Operation(StartOperationCommand command, Business business, User operator) {
        this.operationDate = new Date();
        this.startTime = new Date();
        this.initialCash = command.initialCash();
        this.isOpen = true;
        this.business = business;
        this.openedBy = operator;
        this.totalIncome = 0.0;
    }

    // Constructor simplificado para auto-creación
    public Operation(Business business, User operator, Double initialCash, String notes) {
        this.operationDate = new Date();
        this.startTime = new Date();
        this.initialCash = initialCash;
        this.isOpen = true;
        this.business = business;
        this.openedBy = operator;
        this.totalIncome = 0.0;
        this.notes = notes;
    }

    public void close(User operator, Double finalCash, String notes) {
        this.endTime = new Date();
        this.closedBy = operator;
        this.finalCash = finalCash;
        this.notes = notes;
        this.isOpen = false;
    }

    public void incrementVehicleCount(String vehicleType) {
        this.totalVehicles++;
        switch (vehicleType.toUpperCase()) {
            case "MOTORCYCLE":
                this.totalMotorcycles++;
                break;
            case "CAR":
                this.totalCars++;
                break;
            case "TRUCK":
                this.totalTrucks++;
                break;
        }
    }

    public void addIncome(Double amount) {
        if (this.totalIncome == null) {
            this.totalIncome = 0.0;
        }
        this.totalIncome += amount;
    }

    // Getters
    public Date getOperationDate() {
        return operationDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Double getInitialCash() {
        return initialCash;
    }

    public Double getFinalCash() {
        return finalCash;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public Integer getTotalVehicles() {
        return totalVehicles;
    }

    public Integer getTotalMotorcycles() {
        return totalMotorcycles;
    }

    public Integer getTotalCars() {
        return totalCars;
    }

    public Integer getTotalTrucks() {
        return totalTrucks;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public String getNotes() {
        return notes;
    }

    public Business getBusiness() {
        return business;
    }

    public User getOpenedBy() {
        return openedBy;
    }

    public User getClosedBy() {
        return closedBy;
    }
}

