package com.easypark.platform.accounting.interfaces.rest.transform;

import com.easypark.platform.accounting.domain.model.commands.CreateAccountingRecordCommand;
import com.easypark.platform.accounting.interfaces.rest.resources.CreateAccountingRecordResource;

public class CreateAccountingRecordCommandFromResourceAssembler {

    public static CreateAccountingRecordCommand toCommandFromResource(
            CreateAccountingRecordResource resource,
            Long businessId,
            Long createdBy) {
        return new CreateAccountingRecordCommand(
                businessId,
                resource.recordType(),
                resource.amount(),
                resource.description(),
                resource.category(),
                resource.recordDate(),
                resource.relatedOperationId(),
                createdBy
        );
    }
}

