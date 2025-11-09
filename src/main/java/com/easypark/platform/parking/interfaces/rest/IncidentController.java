package com.easypark.platform.parking.interfaces.rest;

import com.easypark.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.easypark.platform.parking.domain.services.IncidentCommandService;
import com.easypark.platform.parking.domain.services.IncidentQueryService;
import com.easypark.platform.parking.interfaces.rest.resources.CreateIncidentResource;
import com.easypark.platform.parking.interfaces.rest.resources.IncidentResource;
import com.easypark.platform.parking.interfaces.rest.resources.ResolveIncidentResource;
import com.easypark.platform.parking.interfaces.rest.transform.CreateIncidentCommandFromResourceAssembler;
import com.easypark.platform.parking.interfaces.rest.transform.IncidentResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/parking/incidents", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Incidents", description = "Incident Management Endpoints")
@SecurityRequirement(name = "bearerAuth")
public class IncidentController {

    private final IncidentCommandService incidentCommandService;
    private final IncidentQueryService incidentQueryService;

    public IncidentController(IncidentCommandService incidentCommandService,
                             IncidentQueryService incidentQueryService) {
        this.incidentCommandService = incidentCommandService;
        this.incidentQueryService = incidentQueryService;
    }

    @Operation(summary = "Create new incident")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<IncidentResource> createIncident(
            @RequestBody CreateIncidentResource resource,
            Authentication authentication) {

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();
        var reportedBy = userDetails.getId();

        var command = CreateIncidentCommandFromResourceAssembler
                .toCommandFromResource(resource, businessId, reportedBy);

        var incident = incidentCommandService.handle(command);

        if (incident.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var incidentResource = IncidentResourceFromEntityAssembler.toResourceFromEntity(incident.get());
        return new ResponseEntity<>(incidentResource, HttpStatus.CREATED);
    }

    @Operation(summary = "Resolve incident")
    @PatchMapping("/{incidentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<IncidentResource> resolveIncident(
            @PathVariable Long incidentId,
            @RequestBody ResolveIncidentResource resource,
            Authentication authentication) {

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var resolvedBy = userDetails.getId();

        var incident = incidentCommandService.resolveIncident(
                incidentId, resolvedBy, resource.resolutionNotes());

        if (incident.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var incidentResource = IncidentResourceFromEntityAssembler.toResourceFromEntity(incident.get());
        return ResponseEntity.ok(incidentResource);
    }

    @Operation(summary = "Get all incidents")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<IncidentResource>> getAllIncidents(Authentication authentication) {
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var incidents = incidentQueryService.getIncidentsByBusinessId(businessId);

        var incidentResources = incidents.stream()
                .map(IncidentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(incidentResources);
    }

    @Operation(summary = "Get pending incidents")
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<IncidentResource>> getPendingIncidents(Authentication authentication) {
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var incidents = incidentQueryService.getPendingIncidentsByBusinessId(businessId);

        var incidentResources = incidents.stream()
                .map(IncidentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(incidentResources);
    }
}

