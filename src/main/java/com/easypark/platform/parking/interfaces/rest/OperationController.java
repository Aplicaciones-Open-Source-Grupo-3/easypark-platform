package com.easypark.platform.parking.interfaces.rest;

import com.easypark.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.easypark.platform.parking.domain.model.queries.GetAllOperationsByBusinessIdQuery;
import com.easypark.platform.parking.domain.model.queries.GetOperationByIdQuery;
import com.easypark.platform.parking.domain.model.queries.GetTodayOperationByBusinessIdQuery;
import com.easypark.platform.parking.domain.services.OperationCommandService;
import com.easypark.platform.parking.domain.services.OperationQueryService;
import com.easypark.platform.parking.interfaces.rest.resources.CloseOperationResource;
import com.easypark.platform.parking.interfaces.rest.resources.ErrorResponse;
import com.easypark.platform.parking.interfaces.rest.resources.OperationResource;
import com.easypark.platform.parking.interfaces.rest.resources.StartOperationResource;
import com.easypark.platform.parking.interfaces.rest.transform.CloseOperationCommandFromResourceAssembler;
import com.easypark.platform.parking.interfaces.rest.transform.OperationResourceFromEntityAssembler;
import com.easypark.platform.parking.interfaces.rest.transform.StartOperationCommandFromResourceAssembler;
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
@RequestMapping(value = "/api/v1/parking/operations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Operations", description = "Daily Operations Management Endpoints")
@SecurityRequirement(name = "bearerAuth")
public class OperationController {

    private final OperationCommandService operationCommandService;
    private final OperationQueryService operationQueryService;

    public OperationController(OperationCommandService operationCommandService,
                              OperationQueryService operationQueryService) {
        this.operationCommandService = operationCommandService;
        this.operationQueryService = operationQueryService;
    }

    @Operation(summary = "Start daily operations")
    @PostMapping("/start")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<?> startOperation(
            @RequestBody(required = false) StartOperationResource resource,
            Authentication authentication) {

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();
        var operatorId = userDetails.getId();

        // Si no se proporciona resource, usar valores por defecto
        if (resource == null) {
            resource = new StartOperationResource(0.0);
        }

        var command = StartOperationCommandFromResourceAssembler
                .toCommandFromResource(resource, businessId, operatorId);

        try {
            var operation = operationCommandService.handle(command);

            if (operation.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("No se pudo iniciar la operación"));
            }

            var operationResource = OperationResourceFromEntityAssembler.toResourceFromEntity(operation.get());
            return new ResponseEntity<>(operationResource, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Close daily operations")
    @PostMapping("/{operationId}/close")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<?> closeOperation(
            @PathVariable Long operationId,
            @RequestBody(required = false) CloseOperationResource resource,
            Authentication authentication) {

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var operatorId = userDetails.getId();

        // Si no se proporciona resource, usar valores por defecto
        if (resource == null) {
            resource = new CloseOperationResource(0.0, "Operación cerrada");
        }

        var command = CloseOperationCommandFromResourceAssembler
                .toCommandFromResource(operationId, resource, operatorId);

        try {
            var operation = operationCommandService.handle(command);

            if (operation.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("No se pudo cerrar la operación"));
            }

            var operationResource = OperationResourceFromEntityAssembler.toResourceFromEntity(operation.get());
            return ResponseEntity.ok(operationResource);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Close current operation")
    @PostMapping("/close")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<?> closeCurrentOperation(
            @RequestBody(required = false) CloseOperationResource resource,
            Authentication authentication) {

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();
        var operatorId = userDetails.getId();

        // Buscar la operación abierta actual
        var query = new GetTodayOperationByBusinessIdQuery(businessId);
        var currentOperation = operationQueryService.handle(query);

        if (currentOperation.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("No hay una operación abierta para cerrar"));
        }

        if (!currentOperation.get().getIsOpen()) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("La operación de hoy ya está cerrada"));
        }

        // Si no se proporciona resource, usar valores por defecto
        if (resource == null) {
            resource = new CloseOperationResource(0.0, "Operación cerrada");
        }

        var command = CloseOperationCommandFromResourceAssembler
                .toCommandFromResource(currentOperation.get().getId(), resource, operatorId);

        try {
            var operation = operationCommandService.handle(command);

            if (operation.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("No se pudo cerrar la operación"));
            }

            var operationResource = OperationResourceFromEntityAssembler.toResourceFromEntity(operation.get());
            return ResponseEntity.ok(operationResource);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Get today's operation")
    @GetMapping("/today")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<OperationResource> getTodayOperation(Authentication authentication) {
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var query = new GetTodayOperationByBusinessIdQuery(businessId);
        var operation = operationQueryService.handle(query);

        if (operation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var operationResource = OperationResourceFromEntityAssembler.toResourceFromEntity(operation.get());
        return ResponseEntity.ok(operationResource);
    }

    @Operation(summary = "Get operation by ID")
    @GetMapping("/{operationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<?> getOperationById(
            @PathVariable Long operationId,
            Authentication authentication) {

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var query = new GetOperationByIdQuery(operationId);
        var operation = operationQueryService.handle(query);

        if (operation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Operación no encontrada"));
        }

        // Verificar que la operación pertenece al negocio del usuario
        if (!operation.get().getBusiness().getId().equals(businessId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("No tiene permisos para acceder a esta operación"));
        }

        var operationResource = OperationResourceFromEntityAssembler.toResourceFromEntity(operation.get());
        return ResponseEntity.ok(operationResource);
    }

    @Operation(summary = "Get operations history")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<OperationResource>> getAllOperations(Authentication authentication) {
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var query = new GetAllOperationsByBusinessIdQuery(businessId);
        var operations = operationQueryService.handle(query);

        var operationResources = operations.stream()
                .map(OperationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(operationResources);
    }
}

