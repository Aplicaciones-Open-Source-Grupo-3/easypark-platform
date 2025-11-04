package com.easypark.platform.iam.application.internal.queryservices;

import com.easypark.platform.iam.domain.model.aggregates.User;
import com.easypark.platform.iam.domain.model.queries.GetUserByUsernameQuery;
import com.easypark.platform.iam.domain.services.UserQueryService;
import com.easypark.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(GetUserByUsernameQuery query) {
        return userRepository.findByUsername(query.username());
    }
}

