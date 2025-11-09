package com.easypark.platform.clients.infrastructure.persistence.jpa.repositories;

import com.easypark.platform.clients.domain.model.aggregates.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    List<Subscriber> findByBusinessId(Long businessId);
    List<Subscriber> findByBusinessIdAndIsActiveTrue(Long businessId);
    Optional<Subscriber> findByIdAndBusinessId(Long id, Long businessId);
    Optional<Subscriber> findByEmailAndBusinessId(String email, Long businessId);
}

