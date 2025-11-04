package com.easypark.platform.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(
    Long id,
    String username,
    String email,
    String name,
    String role,
    Long businessId,
    String businessName,
    String token
) {
}

