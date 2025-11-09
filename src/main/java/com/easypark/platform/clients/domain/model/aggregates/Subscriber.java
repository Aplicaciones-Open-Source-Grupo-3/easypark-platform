package com.easypark.platform.clients.domain.model.aggregates;

import com.easypark.platform.clients.domain.model.commands.CreateSubscriberCommand;
import com.easypark.platform.clients.domain.model.commands.UpdateSubscriberCommand;
import com.easypark.platform.iam.domain.model.aggregates.Business;
import com.easypark.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Subscriber extends AuditableAbstractAggregateRoot<Subscriber> {

    @NotBlank
    @Size(max = 100)
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Email
    @Size(max = 100)
    @Column(unique = true)
    private String email;

    @Size(max = 20)
    @Column(nullable = false)
    private String phone;

    @Size(max = 200)
    private String address;

    @Size(max = 20)
    @Column(name = "vehicle_license_plate")
    private String vehicleLicensePlate;

    @Size(max = 50)
    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "subscription_months")
    private Integer subscriptionMonths;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "start_date")
    private java.util.Date startDate;

    @Column(name = "payment_date")
    private java.util.Date paymentDate;

    @Size(max = 20)
    @Column(name = "status")
    private String status = "active";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(nullable = false)
    private Boolean isActive = true;

    public Subscriber() {
    }

    public Subscriber(CreateSubscriberCommand command, Business business) {
        this.fullName = command.fullName();
        this.email = command.email();
        this.phone = command.phone();
        this.vehicleLicensePlate = command.vehicleLicensePlate();
        this.subscriptionMonths = command.subscriptionMonths();
        this.amount = command.amount();
        this.startDate = command.startDate();
        this.paymentDate = command.paymentDate();
        this.status = "active";
        this.business = business;
        this.isActive = true;
    }

    public void update(UpdateSubscriberCommand command) {
        this.fullName = command.fullName();
        this.email = command.email();
        this.phone = command.phone();
        this.address = command.address();
        this.vehicleLicensePlate = command.vehicleLicensePlate();
        this.vehicleType = command.vehicleType();
    }

    public void deactivate() {
        this.isActive = false;
    }

    // Getters
    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getVehicleLicensePlate() {
        return vehicleLicensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public Integer getSubscriptionMonths() {
        return subscriptionMonths;
    }

    public Double getAmount() {
        return amount;
    }

    public java.util.Date getStartDate() {
        return startDate;
    }

    public java.util.Date getPaymentDate() {
        return paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public Business getBusiness() {
        return business;
    }

    public Boolean getIsActive() {
        return isActive;
    }
}

