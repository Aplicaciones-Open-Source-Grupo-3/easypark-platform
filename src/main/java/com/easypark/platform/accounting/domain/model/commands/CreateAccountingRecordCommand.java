package com.easypark.platform.accounting.domain.model.commands;

import com.easypark.platform.accounting.domain.model.valueobjects.RecordType;

import java.util.Date;

public record CreateAccountingRecordCommand(
    Long businessId,
    RecordType recordType,
    Double amount,
    String description,
    String category,
    Date recordDate,
    Long relatedOperationId,
    Long createdBy
) {
}

