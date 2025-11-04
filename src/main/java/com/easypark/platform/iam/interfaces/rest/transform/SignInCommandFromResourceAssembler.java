package com.easypark.platform.iam.interfaces.rest.transform;

import com.easypark.platform.iam.domain.model.commands.SignInCommand;
import com.easypark.platform.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {

    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(resource.username(), resource.password());
    }
}

