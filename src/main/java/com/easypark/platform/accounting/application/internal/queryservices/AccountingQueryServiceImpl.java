package com.easypark.platform.accounting.application.internal.queryservices;

import com.easypark.platform.accounting.domain.model.aggregates.AccountingRecord;
import com.easypark.platform.accounting.domain.model.queries.GetAllRecordsByBusinessIdQuery;
import com.easypark.platform.accounting.domain.model.queries.GetRecordByIdQuery;
import com.easypark.platform.accounting.domain.services.AccountingQueryService;
import com.easypark.platform.accounting.infrastructure.persistence.jpa.repositories.AccountingRecordRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AccountingQueryServiceImpl implements AccountingQueryService {

    private final AccountingRecordRepository accountingRecordRepository;

    public AccountingQueryServiceImpl(AccountingRecordRepository accountingRecordRepository) {
        this.accountingRecordRepository = accountingRecordRepository;
    }

    @Override
    public Optional<AccountingRecord> handle(GetRecordByIdQuery query) {
        return accountingRecordRepository.findById(query.recordId());
    }

    @Override
    public List<AccountingRecord> handle(GetAllRecordsByBusinessIdQuery query) {
        return accountingRecordRepository.findByBusinessIdOrderByRecordDateDesc(query.businessId());
    }

    @Override
    public Double getTotalRevenue(Long businessId) {
        Double total = accountingRecordRepository.getTotalIncomeByBusinessId(businessId);
        return total != null ? total : 0.0;
    }

    @Override
    public Double getRevenueByDateRange(Long businessId, Date startDate, Date endDate) {
        Double total = accountingRecordRepository.getIncomeByBusinessIdAndDateRange(businessId, startDate, endDate);
        return total != null ? total : 0.0;
    }
}

