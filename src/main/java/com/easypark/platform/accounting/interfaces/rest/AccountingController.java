package com.easypark.platform.accounting.interfaces.rest;

import com.easypark.platform.accounting.domain.model.queries.GetAllRecordsByBusinessIdQuery;
import com.easypark.platform.accounting.domain.model.queries.GetRecordByIdQuery;
import com.easypark.platform.accounting.domain.services.AccountingCommandService;
import com.easypark.platform.accounting.domain.services.AccountingQueryService;
import com.easypark.platform.accounting.interfaces.rest.resources.AccountingRecordResource;
import com.easypark.platform.accounting.interfaces.rest.resources.CreateAccountingRecordResource;
import com.easypark.platform.accounting.interfaces.rest.resources.RevenueResource;
import com.easypark.platform.accounting.interfaces.rest.transform.AccountingRecordResourceFromEntityAssembler;
import com.easypark.platform.accounting.interfaces.rest.transform.CreateAccountingRecordCommandFromResourceAssembler;
import com.easypark.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/accounting/records", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Accounting", description = "Accounting Management Endpoints")
@SecurityRequirement(name = "bearerAuth")
public class AccountingController {

    private final AccountingCommandService accountingCommandService;
    private final AccountingQueryService accountingQueryService;

    public AccountingController(AccountingCommandService accountingCommandService,
                               AccountingQueryService accountingQueryService) {
        this.accountingCommandService = accountingCommandService;
        this.accountingQueryService = accountingQueryService;
    }

    @Operation(summary = "Create accounting record")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<AccountingRecordResource> createRecord(
            @RequestBody CreateAccountingRecordResource resource,
            Authentication authentication) {

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();
        var createdBy = userDetails.getId();

        var command = CreateAccountingRecordCommandFromResourceAssembler
                .toCommandFromResource(resource, businessId, createdBy);

        var record = accountingCommandService.handle(command);

        if (record.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var recordResource = AccountingRecordResourceFromEntityAssembler.toResourceFromEntity(record.get());
        return new ResponseEntity<>(recordResource, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all accounting records")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<AccountingRecordResource>> getAllRecords(Authentication authentication) {
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var query = new GetAllRecordsByBusinessIdQuery(businessId);
        var records = accountingQueryService.handle(query);

        var recordResources = records.stream()
                .map(AccountingRecordResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(recordResources);
    }

    @Operation(summary = "Get record by ID")
    @GetMapping("/{recordId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<AccountingRecordResource> getRecordById(
            @PathVariable Long recordId,
            Authentication authentication) {

        var query = new GetRecordByIdQuery(recordId);
        var record = accountingQueryService.handle(query);

        if (record.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Verificar que el registro pertenece al negocio del usuario
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (!record.get().getBusiness().getId().equals(userDetails.getBusinessId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var recordResource = AccountingRecordResourceFromEntityAssembler.toResourceFromEntity(record.get());
        return ResponseEntity.ok(recordResource);
    }

    @Operation(summary = "Get total revenue")
    @GetMapping("/revenue/total")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<RevenueResource> getTotalRevenue(Authentication authentication) {
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var totalRevenue = accountingQueryService.getTotalRevenue(businessId);
        return ResponseEntity.ok(new RevenueResource(totalRevenue));
    }

    @Operation(summary = "Get revenue by date range")
    @GetMapping("/revenue/by-date")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<RevenueResource> getRevenueByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            Authentication authentication) {

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var revenue = accountingQueryService.getRevenueByDateRange(businessId, startDate, endDate);
        return ResponseEntity.ok(new RevenueResource(revenue));
    }
}

