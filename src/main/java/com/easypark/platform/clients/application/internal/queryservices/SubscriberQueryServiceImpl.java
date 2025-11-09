package com.easypark.platform.clients.application.internal.queryservices;

import com.easypark.platform.clients.domain.model.aggregates.Subscriber;
import com.easypark.platform.clients.domain.model.queries.GetAllSubscribersByBusinessIdQuery;
import com.easypark.platform.clients.domain.model.queries.GetSubscriberByIdQuery;
import com.easypark.platform.clients.domain.services.SubscriberQueryService;
import com.easypark.platform.clients.infrastructure.persistence.jpa.repositories.SubscriberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriberQueryServiceImpl implements SubscriberQueryService {

    private final SubscriberRepository subscriberRepository;

    public SubscriberQueryServiceImpl(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public Optional<Subscriber> handle(GetSubscriberByIdQuery query) {
        return subscriberRepository.findById(query.subscriberId());
    }

    @Override
    public List<Subscriber> handle(GetAllSubscribersByBusinessIdQuery query) {
        return subscriberRepository.findByBusinessIdAndIsActiveTrue(query.businessId());
    }
}

