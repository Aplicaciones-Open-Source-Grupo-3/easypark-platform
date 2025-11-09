package com.easypark.platform.parking.domain.services;

import com.easypark.platform.parking.domain.model.aggregates.Operation;
import com.easypark.platform.parking.domain.model.queries.GetAllOperationsByBusinessIdQuery;
import com.easypark.platform.parking.domain.model.queries.GetOperationByIdQuery;
import com.easypark.platform.parking.domain.model.queries.GetTodayOperationByBusinessIdQuery;

import java.util.List;
import java.util.Optional;

public interface OperationQueryService {
    Optional<Operation> handle(GetTodayOperationByBusinessIdQuery query);
    List<Operation> handle(GetAllOperationsByBusinessIdQuery query);
    Optional<Operation> handle(GetOperationByIdQuery query);
}

