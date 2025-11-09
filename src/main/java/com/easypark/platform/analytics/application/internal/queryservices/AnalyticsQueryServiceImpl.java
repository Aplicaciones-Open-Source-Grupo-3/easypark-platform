package com.easypark.platform.analytics.application.internal.queryservices;

import com.easypark.platform.accounting.domain.model.valueobjects.RecordType;
import com.easypark.platform.accounting.infrastructure.persistence.jpa.repositories.AccountingRecordRepository;
import com.easypark.platform.analytics.domain.services.AnalyticsQueryService;
import com.easypark.platform.analytics.interfaces.rest.resources.AnalyticsStatsResource;
import com.easypark.platform.analytics.interfaces.rest.resources.OccupancyRateResource;
import com.easypark.platform.clients.infrastructure.persistence.jpa.repositories.SubscriberRepository;
import com.easypark.platform.parking.domain.model.valueobjects.IncidentState;
import com.easypark.platform.parking.domain.model.valueobjects.VehicleStatus;
import com.easypark.platform.parking.infrastructure.persistence.jpa.repositories.IncidentRepository;
import com.easypark.platform.parking.infrastructure.persistence.jpa.repositories.ParkingSpaceRepository;
import com.easypark.platform.parking.infrastructure.persistence.jpa.repositories.VehicleRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class AnalyticsQueryServiceImpl implements AnalyticsQueryService {

    private final VehicleRepository vehicleRepository;
    private final AccountingRecordRepository accountingRecordRepository;
    private final SubscriberRepository subscriberRepository;
    private final IncidentRepository incidentRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;

    public AnalyticsQueryServiceImpl(VehicleRepository vehicleRepository,
                                    AccountingRecordRepository accountingRecordRepository,
                                    SubscriberRepository subscriberRepository,
                                    IncidentRepository incidentRepository,
                                    ParkingSpaceRepository parkingSpaceRepository) {
        this.vehicleRepository = vehicleRepository;
        this.accountingRecordRepository = accountingRecordRepository;
        this.subscriberRepository = subscriberRepository;
        this.incidentRepository = incidentRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    @Override
    public AnalyticsStatsResource getGeneralStats(Long businessId) {
        try {
            // ✅ Ingresos de HOY usando Accounting (solo INCOME)
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date todayStart = cal.getTime();

            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            Date todayEnd = cal.getTime();

            // Ingresos de hoy
            Double todayRevenue = accountingRecordRepository
                    .getIncomeByBusinessIdAndDateRange(businessId, todayStart, todayEnd);
            if (todayRevenue == null) todayRevenue = 0.0;

            // ✅ Vehículos de hoy (contamos registros INCOME de hoy en Accounting)
            Long totalVehiclesToday = 0L;
            try {
                var todayRecords = accountingRecordRepository
                        .findByBusinessIdAndRecordType(businessId, RecordType.INCOME);

                if (todayRecords != null) {
                    totalVehiclesToday = todayRecords.stream()
                            .filter(r -> r.getRecordDate() != null &&
                                       !r.getRecordDate().before(todayStart) &&
                                       !r.getRecordDate().after(todayEnd))
                            .count();
                }
            } catch (Exception e) {
                System.err.println("Error al contar vehículos de hoy: " + e.getMessage());
                totalVehiclesToday = 0L;
            }

            // Vehículos dentro actualmente (desde Parking)
            Long vehiclesInside = 0L;
            try {
                var insideVehicles = vehicleRepository.findByBusinessIdAndStatus(businessId, VehicleStatus.INSIDE);
                if (insideVehicles != null) {
                    vehiclesInside = (long) insideVehicles.size();
                }
            } catch (Exception e) {
                System.err.println("Error al obtener vehículos dentro: " + e.getMessage());
                vehiclesInside = 0L;
            }

            // ✅ Ingresos del mes usando Accounting (últimos 30 días)
            Date monthStart = Date.from(Instant.now().minus(30, ChronoUnit.DAYS));
            Date monthEnd = new Date();
            Double monthRevenue = accountingRecordRepository
                    .getIncomeByBusinessIdAndDateRange(businessId, monthStart, monthEnd);
            if (monthRevenue == null) monthRevenue = 0.0;

            // Total de suscriptores
            Integer totalSubscribers = 0;
            try {
                var subscribers = subscriberRepository.findByBusinessIdAndIsActiveTrue(businessId);
                if (subscribers != null) {
                    totalSubscribers = subscribers.size();
                }
            } catch (Exception e) {
                System.err.println("Error al obtener suscriptores: " + e.getMessage());
                totalSubscribers = 0;
            }

            // Incidentes
            Long totalIncidents = 0L;
            Long pendingIncidents = 0L;
            try {
                var incidents = incidentRepository.findByBusinessIdOrderByReportedAtDesc(businessId);
                if (incidents != null) {
                    totalIncidents = (long) incidents.size();
                }

                var pending = incidentRepository
                        .findByBusinessIdAndStateOrderByReportedAtDesc(businessId, IncidentState.PENDING);
                if (pending != null) {
                    pendingIncidents = (long) pending.size();
                }
            } catch (Exception e) {
                System.err.println("Error al obtener incidentes: " + e.getMessage());
                totalIncidents = 0L;
                pendingIncidents = 0L;
            }

            return new AnalyticsStatsResource(
                    totalVehiclesToday,
                    vehiclesInside,
                    todayRevenue,
                    monthRevenue,
                    totalSubscribers,
                    totalIncidents,
                    pendingIncidents
            );

        } catch (Exception e) {
            System.err.println("Error general en getGeneralStats: " + e.getMessage());
            e.printStackTrace();

            // Retornar estadísticas en cero en caso de error crítico
            return new AnalyticsStatsResource(0L, 0L, 0.0, 0.0, 0, 0L, 0L);
        }
    }

    @Override
    public Map<String, Double> getRevenueTrend(Long businessId, Date startDate, Date endDate) {
        // ✅ Obtener solo registros de tipo INCOME de Accounting
        var allRecords = accountingRecordRepository.findByBusinessIdAndRecordType(businessId, RecordType.INCOME);

        Map<String, Double> trend = new LinkedHashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Filtrar por rango de fechas y agrupar por día
        for (var record : allRecords) {
            if (record.getRecordDate() != null &&
                !record.getRecordDate().before(startDate) &&
                !record.getRecordDate().after(endDate)) {

                String dateKey = dateFormat.format(record.getRecordDate());

                // Sumar el monto pagado (amountPaid) de cada registro
                Double amount = record.getAmountPaid() != null ? record.getAmountPaid() : record.getAmount();
                trend.merge(dateKey, amount, Double::sum);
            }
        }

        // Ordenar por fecha y retornar
        return new TreeMap<>(trend);
    }

    @Override
    public OccupancyRateResource getOccupancyRate(Long businessId) {
        var allSpaces = parkingSpaceRepository.findByBusinessId(businessId);
        var totalSpaces = allSpaces.size();
        var occupiedSpaces = (int) allSpaces.stream().filter(space -> space.getIsOccupied()).count();
        var availableSpaces = totalSpaces - occupiedSpaces;
        var occupancyPercentage = totalSpaces > 0 ? (occupiedSpaces * 100.0 / totalSpaces) : 0.0;

        return new OccupancyRateResource(
                totalSpaces,
                occupiedSpaces,
                availableSpaces,
                occupancyPercentage
        );
    }

    @Override
    public Map<Integer, Integer> getPeakHours(Long businessId, Date date) {
        // ✅ Usar registros de Accounting (INCOME) para determinar horas pico
        var incomeRecords = accountingRecordRepository.findByBusinessIdAndRecordType(businessId, RecordType.INCOME);

        Map<Integer, Integer> hourlyCount = new TreeMap<>();

        Calendar targetCal = Calendar.getInstance();
        targetCal.setTime(date);
        int targetYear = targetCal.get(Calendar.YEAR);
        int targetMonth = targetCal.get(Calendar.MONTH);
        int targetDay = targetCal.get(Calendar.DAY_OF_MONTH);

        for (var record : incomeRecords) {
            if (record.getEntryTime() != null) {
                Calendar recordCal = Calendar.getInstance();
                recordCal.setTime(record.getEntryTime());

                // Verificar si es del mismo día
                if (recordCal.get(Calendar.YEAR) == targetYear &&
                    recordCal.get(Calendar.MONTH) == targetMonth &&
                    recordCal.get(Calendar.DAY_OF_MONTH) == targetDay) {

                    int hour = recordCal.get(Calendar.HOUR_OF_DAY);
                    hourlyCount.merge(hour, 1, Integer::sum);
                }
            }
        }

        // Si no hay datos de entryTime, intentar con recordDate
        if (hourlyCount.isEmpty()) {
            for (var record : incomeRecords) {
                if (record.getRecordDate() != null) {
                    Calendar recordCal = Calendar.getInstance();
                    recordCal.setTime(record.getRecordDate());

                    if (recordCal.get(Calendar.YEAR) == targetYear &&
                        recordCal.get(Calendar.MONTH) == targetMonth &&
                        recordCal.get(Calendar.DAY_OF_MONTH) == targetDay) {

                        int hour = recordCal.get(Calendar.HOUR_OF_DAY);
                        hourlyCount.merge(hour, 1, Integer::sum);
                    }
                }
            }
        }

        return hourlyCount;
    }
}

