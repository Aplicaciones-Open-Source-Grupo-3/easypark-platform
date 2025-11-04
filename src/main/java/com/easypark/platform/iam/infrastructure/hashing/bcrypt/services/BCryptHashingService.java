package com.easypark.platform.iam.infrastructure.hashing.bcrypt.services;

import com.easypark.platform.iam.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface BCryptHashingService extends HashingService, PasswordEncoder {
}

