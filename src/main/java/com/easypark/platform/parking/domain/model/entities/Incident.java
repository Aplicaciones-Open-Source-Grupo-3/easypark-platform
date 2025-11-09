package com.easypark.platform.parking.domain.model.entities;

import com.easypark.platform.iam.domain.model.aggregates.Business;
import com.easypark.platform.parking.domain.model.aggregates.Vehicle;
import com.easypark.platform.parking.domain.model.valueobjects.IncidentState;
import com.easypark.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
public class Incident extends AuditableAbstractAggregateRoot<Incident> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @NotBlank
    @Size(max = 50)
    @Column(name = "incident_type", nullable = false)
    private String incidentType;

    @Column(name = "location", length = 100)
    private String location;

    @NotBlank
    @Size(max = 1000)
    @Column(nullable = false, length = 1000)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IncidentState state;

    @NotNull
    @Column(name = "reported_at", nullable = false)
    private Date reportedAt;

    @Column(name = "reported_by", nullable = false)
    private Long reportedBy;

    @Column(name = "resolved_at")
    private Date resolvedAt;

    @Column(name = "resolved_by")
    private Long resolvedBy;

    @Column(name = "resolution_notes", length = 1000)
    private String resolutionNotes;

    public Incident() {
    }

    public Incident(Vehicle vehicle, Business business, Long reportedBy, String incidentType, String location, String description) {
        this.vehicle = vehicle;
        this.business = business;
        this.reportedBy = reportedBy;
        this.incidentType = incidentType;
        this.location = location;
        this.description = description;
        this.state = IncidentState.PENDING;
        this.reportedAt = new Date();
    }

    public void resolve(Long resolvedBy, String resolutionNotes) {
        this.state = IncidentState.RESOLVED;
        this.resolvedAt = new Date();
        this.resolvedBy = resolvedBy;
        this.resolutionNotes = resolutionNotes;
    }

    public void cancel() {
        this.state = IncidentState.CANCELLED;
    }

    // Getters
    public Vehicle getVehicle() {
        return vehicle;
    }

    public Business getBusiness() {
        return business;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public IncidentState getState() {
        return state;
    }

    public Date getReportedAt() {
        return reportedAt;
    }

    public Long getReportedBy() {
        return reportedBy;
    }

    public Date getResolvedAt() {
        return resolvedAt;
    }

    public Long getResolvedBy() {
        return resolvedBy;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }
}
