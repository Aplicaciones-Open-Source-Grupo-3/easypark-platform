package com.easypark.platform.parking.interfaces.rest;

import com.easypark.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.easypark.platform.parking.infrastructure.persistence.jpa.repositories.VehicleDebtRepository;
import com.easypark.platform.parking.interfaces.rest.resources.PayDebtResource;
import com.easypark.platform.parking.interfaces.rest.resources.VehicleDebtResource;
import com.easypark.platform.parking.interfaces.rest.transform.VehicleDebtResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/parking/debts", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Vehicle Debts", description = "Vehicle Debt Management Endpoints")
@SecurityRequirement(name = "bearerAuth")
public class VehicleDebtController {

    private final VehicleDebtRepository vehicleDebtRepository;

    public VehicleDebtController(VehicleDebtRepository vehicleDebtRepository) {
        this.vehicleDebtRepository = vehicleDebtRepository;
    }

    @Operation(summary = "Get all pending debts")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<VehicleDebtResource>> getPendingDebts(Authentication authentication) {
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var debts = vehicleDebtRepository.findByBusinessIdAndIsPaidFalseOrderByDebtDateDesc(businessId);

        var debtResources = debts.stream()
                .map(VehicleDebtResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(debtResources);
    }

    @Operation(summary = "Get all debts (including paid)")
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<VehicleDebtResource>> getAllDebts(Authentication authentication) {
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var debts = vehicleDebtRepository.findByBusinessIdOrderByDebtDateDesc(businessId);

        var debtResources = debts.stream()
                .map(VehicleDebtResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(debtResources);
    }

    @Operation(summary = "Mark debt as paid")
    @PostMapping("/{debtId}/pay")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<VehicleDebtResource> payDebt(
            @PathVariable Long debtId,
            @RequestBody PayDebtResource resource,
            Authentication authentication) {

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var paidBy = userDetails.getId();

        var debt = vehicleDebtRepository.findById(debtId);

        if (debt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Verificar que la deuda pertenece al negocio del usuario
        if (!debt.get().getBusiness().getId().equals(userDetails.getBusinessId())) {
            return ResponseEntity.status(403).build();
        }

        debt.get().markAsPaid(paidBy, resource.paymentAmount(), resource.notes());
        var updatedDebt = vehicleDebtRepository.save(debt.get());

        var debtResource = VehicleDebtResourceFromEntityAssembler.toResourceFromEntity(updatedDebt);
        return ResponseEntity.ok(debtResource);
    }
}

