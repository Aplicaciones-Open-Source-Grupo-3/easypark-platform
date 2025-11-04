package com.easypark.platform.iam.interfaces.rest.transform;

import com.easypark.platform.iam.domain.model.aggregates.User;
import com.easypark.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {

    public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
        var role = user.getRoles().isEmpty() ? "ROLE_OPERATOR"
                : user.getRoles().stream().findFirst().get().getStringName();

        return new AuthenticatedUserResource(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getName(),
            role,
            user.getBusiness().getId(),
            user.getBusiness().getBusinessName(),
            token
        );
    }
}

