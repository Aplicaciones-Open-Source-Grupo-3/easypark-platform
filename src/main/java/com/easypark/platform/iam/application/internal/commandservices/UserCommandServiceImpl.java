package com.easypark.platform.iam.application.internal.commandservices;

import com.easypark.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.easypark.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.easypark.platform.iam.domain.model.aggregates.Business;
import com.easypark.platform.iam.domain.model.aggregates.User;
import com.easypark.platform.iam.domain.model.commands.SignInCommand;
import com.easypark.platform.iam.domain.model.commands.SignUpCommand;
import com.easypark.platform.iam.domain.model.valueobjects.Roles;
import com.easypark.platform.iam.domain.services.UserCommandService;
import com.easypark.platform.iam.infrastructure.persistence.jpa.repositories.BusinessRepository;
import com.easypark.platform.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.easypark.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final RoleRepository roleRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public UserCommandServiceImpl(UserRepository userRepository,
                                 BusinessRepository businessRepository,
                                 RoleRepository roleRepository,
                                 HashingService hashingService,
                                 TokenService tokenService,
                                 AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
        this.roleRepository = roleRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByUsername(command.adminUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(command.adminEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create business
        var business = new Business(
                command.businessName(),
                command.address(),
                command.phone(),
                command.email(),
                command.taxId(),
                command.maxCapacity(),
                command.motorcycleRate(),
                command.carTruckRate(),
                command.nightRate(),
                command.openingTime(),
                command.closingTime(),
                command.currency()
        );

        var savedBusiness = businessRepository.save(business);

        // Create admin user
        var roles = roleRepository.findByName(Roles.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(new com.easypark.platform.iam.domain.model.entities.Role(Roles.ROLE_ADMIN)));

        var user = new User(
                command.adminUsername(),
                command.adminEmail(),
                hashingService.encode(command.adminPassword()),
                command.adminName(),
                savedBusiness
        );

        user.addRole(roles);

        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(command.username(), command.password())
        );

        String token = tokenService.generateToken(authentication.getName());

        var user = userRepository.findByUsername(command.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return Optional.of(ImmutablePair.of(user, token));
    }
}

