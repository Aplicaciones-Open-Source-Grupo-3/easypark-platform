package com.easypark.platform.iam.infrastructure.hashing.bcrypt.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptHashingServiceImpl extends BCryptPasswordEncoder implements BCryptHashingService {
    public BCryptHashingServiceImpl() {
        super();
    }
}

