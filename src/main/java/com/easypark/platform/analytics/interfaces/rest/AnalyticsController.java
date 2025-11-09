package com.easypark.platform.analytics.interfaces.rest;

import com.easypark.platform.analytics.domain.services.AnalyticsQueryService;
import com.easypark.platform.analytics.interfaces.rest.resources.AnalyticsStatsResource;
import com.easypark.platform.analytics.interfaces.rest.resources.OccupancyRateResource;
import com.easypark.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/analytics", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Analytics", description = "Analytics and Reports Endpoints")
@SecurityRequirement(name = "bearerAuth")
public class AnalyticsController {

    private final AnalyticsQueryService analyticsQueryService;

    public AnalyticsController(AnalyticsQueryService analyticsQueryService) {
        this.analyticsQueryService = analyticsQueryService;
    }

    @Operation(summary = "Get general statistics")
    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<AnalyticsStatsResource> getGeneralStats(Authentication authentication) {
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var stats = analyticsQueryService.getGeneralStats(businessId);
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "Get revenue trend")
    @GetMapping("/revenue-trend")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<Map<String, Double>> getRevenueTrend(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            Authentication authentication) {

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var trend = analyticsQueryService.getRevenueTrend(businessId, startDate, endDate);
        return ResponseEntity.ok(trend);
    }

    @Operation(summary = "Get occupancy rate")
    @GetMapping("/occupancy-rate")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<OccupancyRateResource> getOccupancyRate(Authentication authentication) {
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var occupancyRate = analyticsQueryService.getOccupancyRate(businessId);
        return ResponseEntity.ok(occupancyRate);
    }

    @Operation(summary = "Get peak hours")
    @GetMapping("/peak-hours")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<Map<Integer, Integer>> getPeakHours(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            Authentication authentication) {

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var peakHours = analyticsQueryService.getPeakHours(businessId, date);
        return ResponseEntity.ok(peakHours);
    }
}

