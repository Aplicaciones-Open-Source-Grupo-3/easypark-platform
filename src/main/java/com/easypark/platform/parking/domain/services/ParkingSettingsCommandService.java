package com.easypark.platform.parking.domain.services;

import com.easypark.platform.iam.domain.model.aggregates.Business;
import com.easypark.platform.parking.domain.model.commands.CreateParkingSettingsCommand;

import java.util.Optional;

public interface ParkingSettingsCommandService {
    Optional<Business> handle(CreateParkingSettingsCommand command);
}

