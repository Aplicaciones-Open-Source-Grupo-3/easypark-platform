package com.easypark.platform.clients.domain.services;

import com.easypark.platform.clients.domain.model.aggregates.Subscriber;
import com.easypark.platform.clients.domain.model.queries.GetAllSubscribersByBusinessIdQuery;
import com.easypark.platform.clients.domain.model.queries.GetSubscriberByIdQuery;

import java.util.List;
import java.util.Optional;

public interface SubscriberQueryService {
    Optional<Subscriber> handle(GetSubscriberByIdQuery query);
    List<Subscriber> handle(GetAllSubscribersByBusinessIdQuery query);
}

