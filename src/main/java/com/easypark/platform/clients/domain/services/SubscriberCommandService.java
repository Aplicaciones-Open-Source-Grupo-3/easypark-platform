package com.easypark.platform.clients.domain.services;

import com.easypark.platform.clients.domain.model.aggregates.Subscriber;
import com.easypark.platform.clients.domain.model.commands.CreateSubscriberCommand;
import com.easypark.platform.clients.domain.model.commands.UpdateSubscriberCommand;

import java.util.Optional;

public interface SubscriberCommandService {
    Optional<Subscriber> handle(CreateSubscriberCommand command);
    Optional<Subscriber> handle(UpdateSubscriberCommand command);
    boolean deleteSubscriber(Long subscriberId, Long businessId);
}

