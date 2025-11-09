package com.easypark.platform.accounting.interfaces.rest.resources;

import com.easypark.platform.accounting.domain.model.valueobjects.RecordType;

import java.util.Date;

public record CreateAccountingRecordResource(
    RecordType recordType,
    Double amount,
    String description,
    String category,
    Date recordDate,
    Long relatedOperationId
) {
}
