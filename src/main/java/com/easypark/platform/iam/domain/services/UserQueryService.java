package com.easypark.platform.iam.domain.services;

import com.easypark.platform.iam.domain.model.aggregates.User;
import com.easypark.platform.iam.domain.model.queries.GetUserByUsernameQuery;

import java.util.Optional;

public interface UserQueryService {
    Optional<User> handle(GetUserByUsernameQuery query);
}

