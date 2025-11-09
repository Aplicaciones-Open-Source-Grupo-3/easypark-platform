package com.easypark.platform.clients.interfaces.rest;

import com.easypark.platform.clients.domain.model.queries.GetAllSubscribersByBusinessIdQuery;
import com.easypark.platform.clients.domain.model.queries.GetSubscriberByIdQuery;
import com.easypark.platform.clients.domain.services.SubscriberCommandService;
import com.easypark.platform.clients.domain.services.SubscriberQueryService;
import com.easypark.platform.clients.interfaces.rest.resources.CreateSubscriberResource;
import com.easypark.platform.clients.interfaces.rest.resources.SubscriberResource;
import com.easypark.platform.clients.interfaces.rest.resources.UpdateSubscriberResource;
import com.easypark.platform.clients.interfaces.rest.transform.CreateSubscriberCommandFromResourceAssembler;
import com.easypark.platform.clients.interfaces.rest.transform.SubscriberResourceFromEntityAssembler;
import com.easypark.platform.clients.interfaces.rest.transform.UpdateSubscriberCommandFromResourceAssembler;
import com.easypark.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
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
@RequestMapping(value = "/api/v1/subscribers", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Subscribers", description = "Subscriber Management Endpoints")
@SecurityRequirement(name = "bearerAuth")
public class SubscriberController {

    private final SubscriberCommandService subscriberCommandService;
    private final SubscriberQueryService subscriberQueryService;

    public SubscriberController(SubscriberCommandService subscriberCommandService,
                               SubscriberQueryService subscriberQueryService) {
        this.subscriberCommandService = subscriberCommandService;
        this.subscriberQueryService = subscriberQueryService;
    }

    @Operation(summary = "Create new subscriber")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<SubscriberResource> createSubscriber(
            @RequestBody CreateSubscriberResource resource,
            Authentication authentication) {

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var command = CreateSubscriberCommandFromResourceAssembler
                .toCommandFromResource(resource, businessId);

        var subscriber = subscriberCommandService.handle(command);

        if (subscriber.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var subscriberResource = SubscriberResourceFromEntityAssembler.toResourceFromEntity(subscriber.get());
        return new ResponseEntity<>(subscriberResource, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all subscribers")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<SubscriberResource>> getAllSubscribers(Authentication authentication) {
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var query = new GetAllSubscribersByBusinessIdQuery(businessId);
        var subscribers = subscriberQueryService.handle(query);

        var subscriberResources = subscribers.stream()
                .map(SubscriberResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(subscriberResources);
    }

    @Operation(summary = "Get subscriber by ID")
    @GetMapping("/{subscriberId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<SubscriberResource> getSubscriberById(
            @PathVariable Long subscriberId,
            Authentication authentication) {

        var query = new GetSubscriberByIdQuery(subscriberId);
        var subscriber = subscriberQueryService.handle(query);

        if (subscriber.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Verificar que el suscriptor pertenece al negocio del usuario
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (!subscriber.get().getBusiness().getId().equals(userDetails.getBusinessId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var subscriberResource = SubscriberResourceFromEntityAssembler.toResourceFromEntity(subscriber.get());
        return ResponseEntity.ok(subscriberResource);
    }

    @Operation(summary = "Update subscriber")
    @PutMapping("/{subscriberId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<SubscriberResource> updateSubscriber(
            @PathVariable Long subscriberId,
            @RequestBody UpdateSubscriberResource resource,
            Authentication authentication) {

        var command = UpdateSubscriberCommandFromResourceAssembler
                .toCommandFromResource(subscriberId, resource);

        var subscriber = subscriberCommandService.handle(command);

        if (subscriber.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var subscriberResource = SubscriberResourceFromEntityAssembler.toResourceFromEntity(subscriber.get());
        return ResponseEntity.ok(subscriberResource);
    }

    @Operation(summary = "Delete subscriber")
    @DeleteMapping("/{subscriberId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSubscriber(
            @PathVariable Long subscriberId,
            Authentication authentication) {

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var businessId = userDetails.getBusinessId();

        var deleted = subscriberCommandService.deleteSubscriber(subscriberId, businessId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}

