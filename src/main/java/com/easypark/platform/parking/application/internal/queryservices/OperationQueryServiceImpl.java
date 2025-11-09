package com.easypark.platform.parking.application.internal.queryservices;

import com.easypark.platform.parking.domain.model.aggregates.Operation;
import com.easypark.platform.parking.domain.model.queries.GetAllOperationsByBusinessIdQuery;
import com.easypark.platform.parking.domain.model.queries.GetOperationByIdQuery;
import com.easypark.platform.parking.domain.model.queries.GetTodayOperationByBusinessIdQuery;
import com.easypark.platform.parking.domain.services.OperationQueryService;
import com.easypark.platform.parking.infrastructure.persistence.jpa.repositories.OperationRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OperationQueryServiceImpl implements OperationQueryService {

    private final OperationRepository operationRepository;

    public OperationQueryServiceImpl(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    @Override
    public Optional<Operation> handle(GetTodayOperationByBusinessIdQuery query) {
        return operationRepository.findByBusinessIdAndOperationDate(query.businessId(), new Date());
    }

    @Override
    public List<Operation> handle(GetAllOperationsByBusinessIdQuery query) {
        return operationRepository.findByBusinessIdOrderByOperationDateDesc(query.businessId());
    }

    @Override
    public Optional<Operation> handle(GetOperationByIdQuery query) {
        return operationRepository.findById(query.operationId());
    }
}

