package com.easypark.platform.accounting.application.internal.commandservices;

import com.easypark.platform.accounting.domain.model.aggregates.AccountingRecord;
import com.easypark.platform.accounting.domain.model.commands.CreateAccountingRecordCommand;
import com.easypark.platform.accounting.domain.services.AccountingCommandService;
import com.easypark.platform.accounting.infrastructure.persistence.jpa.repositories.AccountingRecordRepository;
import com.easypark.platform.iam.infrastructure.persistence.jpa.repositories.BusinessRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountingCommandServiceImpl implements AccountingCommandService {

    private final AccountingRecordRepository accountingRecordRepository;
    private final BusinessRepository businessRepository;

    public AccountingCommandServiceImpl(AccountingRecordRepository accountingRecordRepository,
                                       BusinessRepository businessRepository) {
        this.accountingRecordRepository = accountingRecordRepository;
        this.businessRepository = businessRepository;
    }

    @Override
    @Transactional
    public Optional<AccountingRecord> handle(CreateAccountingRecordCommand command) {
        var business = businessRepository.findById(command.businessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        var record = new AccountingRecord(command, business);
        return Optional.of(accountingRecordRepository.save(record));
    }
}

