package com.easypark.platform.accounting.domain.services;

import com.easypark.platform.accounting.domain.model.aggregates.AccountingRecord;
import com.easypark.platform.accounting.domain.model.commands.CreateAccountingRecordCommand;

import java.util.Optional;

public interface AccountingCommandService {
    Optional<AccountingRecord> handle(CreateAccountingRecordCommand command);
}

