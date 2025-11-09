package com.easypark.platform.parking.domain.model.entities;

import com.easypark.platform.iam.domain.model.aggregates.Business;
import com.easypark.platform.parking.domain.model.aggregates.Vehicle;
import com.easypark.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "vehicle_debt")
public class VehicleDebt extends AuditableAbstractAggregateRoot<VehicleDebt> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(name = "regular_hours")
    private Double regularHours;

    @Column(name = "regular_amount")
    private Double regularAmount;

    @Column(name = "night_charge")
    private Double nightCharge;

    @NotNull
    @Column(name = "debt_amount", nullable = false)
    private Double debtAmount;

    @Column(name = "paid_amount")
    private Double paidAmount;

    @Column(name = "remaining_amount")
    private Double remainingAmount;

    @NotNull
    @Column(nullable = false)
    private Boolean isPaid = false;

    @Column(name = "debt_date", nullable = false)
    private Date debtDate;

    @Column(name = "paid_date")
    private Date paidDate;

    @Column(name = "paid_by")
    private Long paidBy;

    @Column(length = 500)
    private String notes;

    public VehicleDebt() {
    }

    public VehicleDebt(Vehicle vehicle, Business business, Double regularHours, Double regularAmount,
                       Double nightCharge, Double debtAmount, Double paidAmount) {
        this.vehicle = vehicle;
        this.business = business;
        this.regularHours = regularHours;
        this.regularAmount = regularAmount;
        this.nightCharge = nightCharge;
        this.debtAmount = debtAmount;
        this.paidAmount = paidAmount != null ? paidAmount : 0.0;
        this.remainingAmount = debtAmount - this.paidAmount;
        this.isPaid = false;
        this.debtDate = new Date();
    }

    public void markAsPaid(Long paidBy, Double additionalPayment, String notes) {
        this.paidAmount += additionalPayment;
        this.remainingAmount = this.debtAmount - this.paidAmount;

        if (this.remainingAmount <= 0) {
            this.isPaid = true;
            this.paidDate = new Date();
            this.paidBy = paidBy;
        }

        this.notes = notes;
    }

    // Getters
    public Vehicle getVehicle() {
        return vehicle;
    }

    public Business getBusiness() {
        return business;
    }

    public Double getRegularHours() {
        return regularHours;
    }

    public Double getRegularAmount() {
        return regularAmount;
    }

    public Double getNightCharge() {
        return nightCharge;
    }

    public Double getDebtAmount() {
        return debtAmount;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public Double getRemainingAmount() {
        return remainingAmount;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public Date getDebtDate() {
        return debtDate;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public Long getPaidBy() {
        return paidBy;
    }

    public String getNotes() {
        return notes;
    }
}

