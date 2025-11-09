package com.easypark.platform.parking.interfaces.rest;

import com.easypark.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.easypark.platform.parking.domain.model.queries.GetAllVehiclesByBusinessIdQuery;
import com.easypark.platform.parking.domain.model.queries.GetVehicleByIdQuery;
import com.easypark.platform.parking.domain.services.VehicleCommandService;
import com.easypark.platform.parking.domain.services.VehicleQueryService;
import com.easypark.platform.parking.interfaces.rest.resources.ErrorResponse;
import com.easypark.platform.parking.interfaces.rest.resources.RegisterVehicleEntryResource;
import com.easypark.platform.parking.interfaces.rest.resources.RegisterVehicleExitResource;
import com.easypark.platform.parking.interfaces.rest.resources.VehicleResource;
import com.easypark.platform.parking.interfaces.rest.transform.RegisterVehicleEntryCommandFromResourceAssembler;
import com.easypark.platform.parking.interfaces.rest.transform.RegisterVehicleExitCommandFromResourceAssembler;
import com.easypark.platform.parking.interfaces.rest.transform.VehicleResourceFromEntityAssembler;
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
@RequestMapping(value = "/api/v1/parking/vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Vehicles", description = "Vehicle Management Endpoints")
@SecurityRequirement(name = "bearerAuth")
public class VehicleController {

    private final VehicleCommandService vehicleCommandService;
    private final VehicleQueryService vehicleQueryService;

    public VehicleController(VehicleCommandService vehicleCommandService,
                            VehicleQueryService vehicleQueryService) {
        this.vehicleCommandService = vehicleCommandService;
        this.vehicleQueryService = vehicleQueryService;
    }

    @Operation(summary = "Register vehicle entry")
    @PostMapping("/entry")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<?> registerEntry(
            @RequestBody RegisterVehicleEntryResource resource,
            Authentication authentication) {

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();
        var operatorId = userDetails.getId();

        var command = RegisterVehicleEntryCommandFromResourceAssembler
                .toCommandFromResource(resource, businessId, operatorId);

        try {
            var vehicle = vehicleCommandService.handle(command);

            if (vehicle.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("No se pudo registrar el vehículo"));
            }

            var vehicleResource = VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle.get());
            return new ResponseEntity<>(vehicleResource, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Register vehicle exit")
    @PostMapping("/{vehicleId}/exit")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<?> registerExit(
            @PathVariable Long vehicleId,
            @RequestBody RegisterVehicleExitResource resource,
            Authentication authentication) {

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var operatorId = userDetails.getId();

        var command = RegisterVehicleExitCommandFromResourceAssembler
                .toCommandFromResource(vehicleId, resource, operatorId);

        try {
            var vehicle = vehicleCommandService.handle(command);

            if (vehicle.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("No se pudo registrar la salida del vehículo"));
            }

            var vehicleResource = VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle.get());
            return ResponseEntity.ok(vehicleResource);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Get all vehicles")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<VehicleResource>> getAllVehicles(Authentication authentication) {
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var query = new GetAllVehiclesByBusinessIdQuery(businessId);
        var vehicles = vehicleQueryService.handle(query);

        var vehicleResources = vehicles.stream()
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(vehicleResources);
    }

    @Operation(summary = "Get vehicles currently inside")
    @GetMapping("/inside")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<VehicleResource>> getVehiclesInside(Authentication authentication) {
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var vehicles = vehicleQueryService.getVehiclesInside(businessId);

        var vehicleResources = vehicles.stream()
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(vehicleResources);
    }

    @Operation(summary = "Get vehicle by ID")
    @GetMapping("/{vehicleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<VehicleResource> getVehicleById(
            @PathVariable Long vehicleId,
            Authentication authentication) {

        var query = new GetVehicleByIdQuery(vehicleId);
        var vehicle = vehicleQueryService.handle(query);

        if (vehicle.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Verificar que el vehículo pertenece al negocio del usuario
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (!vehicle.get().getBusiness().getId().equals(userDetails.getBusinessId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var vehicleResource = VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle.get());
        return ResponseEntity.ok(vehicleResource);
    }

    @Operation(summary = "Delete vehicle")
    @DeleteMapping("/{vehicleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Long vehicleId,
            Authentication authentication) {

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var deleted = vehicleCommandService.deleteVehicle(vehicleId, businessId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}

