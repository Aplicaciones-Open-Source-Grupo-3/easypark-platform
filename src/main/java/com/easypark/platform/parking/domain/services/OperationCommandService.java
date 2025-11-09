package com.easypark.platform.parking.domain.services;

import com.easypark.platform.parking.domain.model.aggregates.Operation;
import com.easypark.platform.parking.domain.model.commands.CloseOperationCommand;
import com.easypark.platform.parking.domain.model.commands.StartOperationCommand;

import java.util.Optional;

public interface OperationCommandService {
    Optional<Operation> handle(StartOperationCommand command);
    Optional<Operation> handle(CloseOperationCommand command);
}

