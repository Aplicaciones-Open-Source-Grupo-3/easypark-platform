package com.easypark.platform.parking.interfaces.rest;

import com.easypark.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.easypark.platform.iam.infrastructure.persistence.jpa.repositories.BusinessRepository;
import com.easypark.platform.parking.domain.services.ParkingSettingsCommandService;
import com.easypark.platform.parking.interfaces.rest.resources.CreateParkingSettingsResource;
import com.easypark.platform.parking.interfaces.rest.resources.ParkingSettingsResource;
import com.easypark.platform.parking.interfaces.rest.transform.CreateParkingSettingsCommandFromResourceAssembler;
import com.easypark.platform.parking.interfaces.rest.transform.ParkingSettingsResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/parking/settings", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Parking Settings", description = "Parking Configuration Endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ParkingSettingsController {

    private final ParkingSettingsCommandService parkingSettingsCommandService;
    private final BusinessRepository businessRepository;

    public ParkingSettingsController(ParkingSettingsCommandService parkingSettingsCommandService,
                                    BusinessRepository businessRepository) {
        this.parkingSettingsCommandService = parkingSettingsCommandService;
        this.businessRepository = businessRepository;
    }

    @Operation(summary = "Get parking settings for current business")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ParkingSettingsResource> getParkingSettings(Authentication authentication) {
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var business = businessRepository.findById(businessId);
        if (business.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resource = ParkingSettingsResourceFromEntityAssembler.toResourceFromEntity(business.get());
        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Create or update parking settings for a business")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingSettingsResource> createOrUpdateSettings(
            @RequestBody CreateParkingSettingsResource resource,
            Authentication authentication) {

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var command = CreateParkingSettingsCommandFromResourceAssembler
                .toCommandFromResource(resource, businessId);

        var business = parkingSettingsCommandService.handle(command);

        if (business.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var settingsResource = ParkingSettingsResourceFromEntityAssembler
                .toResourceFromEntity(business.get());

        return new ResponseEntity<>(settingsResource, HttpStatus.CREATED);
    }
}

