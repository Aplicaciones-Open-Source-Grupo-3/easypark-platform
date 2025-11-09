package com.easypark.platform.clients.application.internal.commandservices;

import com.easypark.platform.clients.domain.model.aggregates.Subscriber;
import com.easypark.platform.clients.domain.model.commands.CreateSubscriberCommand;
import com.easypark.platform.clients.domain.model.commands.UpdateSubscriberCommand;
import com.easypark.platform.clients.domain.services.SubscriberCommandService;
import com.easypark.platform.clients.infrastructure.persistence.jpa.repositories.SubscriberRepository;
import com.easypark.platform.iam.infrastructure.persistence.jpa.repositories.BusinessRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SubscriberCommandServiceImpl implements SubscriberCommandService {

    private final SubscriberRepository subscriberRepository;
    private final BusinessRepository businessRepository;

    public SubscriberCommandServiceImpl(SubscriberRepository subscriberRepository,
                                       BusinessRepository businessRepository) {
        this.subscriberRepository = subscriberRepository;
        this.businessRepository = businessRepository;
    }

    @Override
    @Transactional
    public Optional<Subscriber> handle(CreateSubscriberCommand command) {
        var business = businessRepository.findById(command.businessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        // Verificar si el email ya existe
        if (command.email() != null && !command.email().isEmpty()) {
            var existingSubscriber = subscriberRepository.findByEmailAndBusinessId(
                    command.email(), command.businessId());
            if (existingSubscriber.isPresent()) {
                throw new RuntimeException("Subscriber with this email already exists");
            }
        }

        var subscriber = new Subscriber(command, business);
        return Optional.of(subscriberRepository.save(subscriber));
    }

    @Override
    @Transactional
    public Optional<Subscriber> handle(UpdateSubscriberCommand command) {
        var subscriber = subscriberRepository.findById(command.subscriberId())
                .orElseThrow(() -> new RuntimeException("Subscriber not found"));

        subscriber.update(command);
        return Optional.of(subscriberRepository.save(subscriber));
    }

    @Override
    @Transactional
    public boolean deleteSubscriber(Long subscriberId, Long businessId) {
        var subscriber = subscriberRepository.findByIdAndBusinessId(subscriberId, businessId);
        if (subscriber.isPresent()) {
            subscriberRepository.delete(subscriber.get());
            return true;
        }
        return false;
    }
}

