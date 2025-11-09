package com.easypark.platform.accounting.infrastructure.persistence.jpa.repositories;

import com.easypark.platform.accounting.domain.model.aggregates.AccountingRecord;
import com.easypark.platform.accounting.domain.model.valueobjects.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AccountingRecordRepository extends JpaRepository<AccountingRecord, Long> {
    List<AccountingRecord> findByBusinessIdOrderByRecordDateDesc(Long businessId);
    List<AccountingRecord> findByBusinessIdAndRecordType(Long businessId, RecordType recordType);

    @Query("SELECT SUM(r.amount) FROM AccountingRecord r WHERE r.business.id = :businessId AND r.recordType = 'INCOME'")
    Double getTotalIncomeByBusinessId(@Param("businessId") Long businessId);

    @Query("SELECT SUM(r.amount) FROM AccountingRecord r WHERE r.business.id = :businessId AND r.recordType = 'INCOME' AND r.recordDate BETWEEN :startDate AND :endDate")
    Double getIncomeByBusinessIdAndDateRange(@Param("businessId") Long businessId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}

