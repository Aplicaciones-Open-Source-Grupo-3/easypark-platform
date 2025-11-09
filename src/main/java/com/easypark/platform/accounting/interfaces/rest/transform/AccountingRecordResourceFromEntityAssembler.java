package com.easypark.platform.accounting.interfaces.rest.transform;

import com.easypark.platform.accounting.domain.model.aggregates.AccountingRecord;
import com.easypark.platform.accounting.interfaces.rest.resources.AccountingRecordResource;

import java.text.SimpleDateFormat;

public class AccountingRecordResourceFromEntityAssembler {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat operationDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static AccountingRecordResource toResourceFromEntity(AccountingRecord entity) {
        return new AccountingRecordResource(
                entity.getId().toString(),
                entity.getRegistrationNumber(),
                entity.getEntryDate() != null ? dateFormat.format(entity.getEntryDate()) : null,
                entity.getExitDate() != null ? dateFormat.format(entity.getExitDate()) : null,
                entity.getVehicleType(),
                entity.getPlate(),
                entity.getEntryTime() != null ? timeFormat.format(entity.getEntryTime()) : null,
                entity.getExitTime() != null ? timeFormat.format(entity.getExitTime()) : null,
                entity.getAmountPaid(),
                entity.getCurrency(),
                entity.getHoursParked(),
                entity.getHoursToPay(),
                entity.getRatePerHour(),
                entity.getOperationDate() != null ? operationDateFormat.format(entity.getOperationDate()) : null,
                entity.getNightCharge(),
                entity.getBusiness().getId().toString()
        );
    }
}

