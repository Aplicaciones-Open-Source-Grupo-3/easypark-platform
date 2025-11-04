package com.easypark.platform.iam.domain.model.aggregates;

import com.easypark.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Business extends AuditableAbstractAggregateRoot<Business> {

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false)
    private String businessName;

    @NotBlank
    @Size(max = 300)
    @Column(nullable = false)
    private String address;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false)
    private String phone;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Size(max = 50)
    @Column(unique = true, nullable = false)
    private String taxId;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer maxCapacity;

    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false)
    private Double motorcycleRate;

    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false)
    private Double carTruckRate;

    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false)
    private Double nightRate;

    @NotBlank
    @Size(max = 10)
    @Column(nullable = false)
    private String openingTime;

    @NotBlank
    @Size(max = 10)
    @Column(nullable = false)
    private String closingTime;

    @NotBlank
    @Size(max = 10)
    @Column(nullable = false)
    private String currency;

    public Business() {
    }

    public Business(String businessName, String address, String phone, String email,
                   String taxId, Integer maxCapacity, Double motorcycleRate,
                   Double carTruckRate, Double nightRate, String openingTime,
                   String closingTime, String currency) {
        this.businessName = businessName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.taxId = taxId;
        this.maxCapacity = maxCapacity;
        this.motorcycleRate = motorcycleRate;
        this.carTruckRate = carTruckRate;
        this.nightRate = nightRate;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.currency = currency;
    }

    // Getters
    public String getBusinessName() {
        return businessName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getTaxId() {
        return taxId;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public Double getMotorcycleRate() {
        return motorcycleRate;
    }

    public Double getCarTruckRate() {
        return carTruckRate;
    }

    public Double getNightRate() {
        return nightRate;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public String getCurrency() {
        return currency;
    }

    // Setters
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setMotorcycleRate(Double motorcycleRate) {
        this.motorcycleRate = motorcycleRate;
    }

    public void setCarTruckRate(Double carTruckRate) {
        this.carTruckRate = carTruckRate;
    }

    public void setNightRate(Double nightRate) {
        this.nightRate = nightRate;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

