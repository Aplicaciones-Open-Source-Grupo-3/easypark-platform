package com.easypark.platform.accounting.domain.services;

import com.easypark.platform.accounting.domain.model.aggregates.AccountingRecord;
import com.easypark.platform.accounting.domain.model.queries.GetAllRecordsByBusinessIdQuery;
import com.easypark.platform.accounting.domain.model.queries.GetRecordByIdQuery;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AccountingQueryService {
    Optional<AccountingRecord> handle(GetRecordByIdQuery query);
    List<AccountingRecord> handle(GetAllRecordsByBusinessIdQuery query);
    Double getTotalRevenue(Long businessId);
    Double getRevenueByDateRange(Long businessId, Date startDate, Date endDate);
}

