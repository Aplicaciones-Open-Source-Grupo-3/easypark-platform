package com.easypark.platform.parking.infrastructure.persistence.jpa.repositories;

import com.easypark.platform.parking.domain.model.aggregates.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findByBusinessIdOrderByOperationDateDesc(Long businessId);

    @Query("SELECT o FROM Operation o WHERE o.business.id = :businessId AND o.isOpen = true")
    Optional<Operation> findOpenOperationByBusinessId(@Param("businessId") Long businessId);

    @Query("SELECT o FROM Operation o WHERE o.business.id = :businessId AND DATE(o.operationDate) = DATE(:date)")
    Optional<Operation> findByBusinessIdAndOperationDate(@Param("businessId") Long businessId, @Param("date") Date date);
}

