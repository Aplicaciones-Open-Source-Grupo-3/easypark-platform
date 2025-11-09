package com.easypark.platform.accounting.domain.model.aggregates;

import com.easypark.platform.accounting.domain.model.commands.CreateAccountingRecordCommand;
import com.easypark.platform.accounting.domain.model.valueobjects.RecordType;
import com.easypark.platform.iam.domain.model.aggregates.Business;
import com.easypark.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "accounting_record")
public class AccountingRecord extends AuditableAbstractAggregateRoot<AccountingRecord> {

    @Column(name = "registration_number", length = 50)
    private String registrationNumber;

    @Column(name = "entry_date")
    private Date entryDate;

    @Column(name = "exit_date")
    private Date exitDate;

    @Column(name = "vehicle_type", length = 50)
    private String vehicleType;

    @Column(name = "plate", length = 20)
    private String plate;

    @Column(name = "entry_time")
    private Date entryTime;

    @Column(name = "exit_time")
    private Date exitTime;

    @Column(name = "amount_paid")
    private Double amountPaid;

    @Column(name = "currency", length = 10)
    private String currency;

    @Column(name = "hours_parked")
    private Double hoursParked;

    @Column(name = "hours_to_pay")
    private Double hoursToPay;

    @Column(name = "rate_per_hour")
    private Double ratePerHour;

    @Column(name = "operation_date")
    private Date operationDate;

    @Column(name = "night_charge")
    private Double nightCharge;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "record_type", nullable = false, length = 20)
    private RecordType recordType;

    @NotNull
    @Column(nullable = false)
    private Double amount;

    @Column(length = 500)
    private String description;

    @Column(length = 50)
    private String category;

    @NotNull
    @Column(name = "record_date", nullable = false)
    private Date recordDate;

    @Column(name = "related_operation_id")
    private Long relatedOperationId;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    public AccountingRecord() {
    }

    public AccountingRecord(CreateAccountingRecordCommand command, Business business) {
        this.recordType = command.recordType();
        this.amount = command.amount();
        this.description = command.description();
        this.category = command.category();
        this.recordDate = command.recordDate() != null ? command.recordDate() : new Date();
        this.relatedOperationId = command.relatedOperationId();
        this.createdBy = command.createdBy();
        this.business = business;
    }

    // Getters
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public Date getExitDate() {
        return exitDate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getPlate() {
        return plate;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public Date getExitTime() {
        return exitTime;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getHoursParked() {
        return hoursParked;
    }

    public Double getHoursToPay() {
        return hoursToPay;
    }

    public Double getRatePerHour() {
        return ratePerHour;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public Double getNightCharge() {
        return nightCharge;
    }

    public RecordType getRecordType() {
        return recordType;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public Long getRelatedOperationId() {
        return relatedOperationId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Business getBusiness() {
        return business;
    }

    // Setters
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public void setExitDate(Date exitDate) {
        this.exitDate = exitDate;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setHoursParked(Double hoursParked) {
        this.hoursParked = hoursParked;
    }

    public void setHoursToPay(Double hoursToPay) {
        this.hoursToPay = hoursToPay;
    }

    public void setRatePerHour(Double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public void setNightCharge(Double nightCharge) {
        this.nightCharge = nightCharge;
    }
}

