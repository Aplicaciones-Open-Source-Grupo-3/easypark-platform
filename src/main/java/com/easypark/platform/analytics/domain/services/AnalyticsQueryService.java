package com.easypark.platform.analytics.domain.services;

import com.easypark.platform.analytics.interfaces.rest.resources.AnalyticsStatsResource;
import com.easypark.platform.analytics.interfaces.rest.resources.OccupancyRateResource;

import java.util.Date;
import java.util.Map;

public interface AnalyticsQueryService {
    AnalyticsStatsResource getGeneralStats(Long businessId);
    Map<String, Double> getRevenueTrend(Long businessId, Date startDate, Date endDate);
    OccupancyRateResource getOccupancyRate(Long businessId);
    Map<Integer, Integer> getPeakHours(Long businessId, Date date);
}
