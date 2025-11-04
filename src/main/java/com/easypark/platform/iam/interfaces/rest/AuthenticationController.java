package com.easypark.platform.iam.interfaces.rest;

import com.easypark.platform.iam.domain.services.UserCommandService;
import com.easypark.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.easypark.platform.iam.interfaces.rest.resources.SignInResource;
import com.easypark.platform.iam.interfaces.rest.resources.SignUpResource;
import com.easypark.platform.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.easypark.platform.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import com.easypark.platform.iam.interfaces.rest.transform.SignUpCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/iam/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication Management Endpoints")
public class AuthenticationController {

    private final UserCommandService userCommandService;

    public AuthenticationController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @Operation(summary = "Sign up a new user and business")
    @PostMapping("/sign-up")
    public ResponseEntity<AuthenticatedUserResource> signUp(@RequestBody SignUpResource resource) {
        var signUpCommand = SignUpCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(signUpCommand);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Auto sign-in after sign-up
        var signInCommand = SignInCommandFromResourceAssembler.toCommandFromResource(
            new SignInResource(resource.adminUsername(), resource.adminPassword())
        );

        var authenticatedUser = userCommandService.handle(signInCommand);

        if (authenticatedUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler
            .toResourceFromEntity(authenticatedUser.get().getLeft(), authenticatedUser.get().getRight());

        return new ResponseEntity<>(authenticatedUserResource, HttpStatus.CREATED);
    }

    @Operation(summary = "Sign in a user")
    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticatedUserResource> signIn(@RequestBody SignInResource resource) {
        var signInCommand = SignInCommandFromResourceAssembler.toCommandFromResource(resource);
        var authenticatedUser = userCommandService.handle(signInCommand);

        if (authenticatedUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler
            .toResourceFromEntity(authenticatedUser.get().getLeft(), authenticatedUser.get().getRight());

        return ResponseEntity.ok(authenticatedUserResource);
    }
}

