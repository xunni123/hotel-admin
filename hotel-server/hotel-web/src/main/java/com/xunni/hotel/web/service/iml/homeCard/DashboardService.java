package com.xunni.hotel.web.service.iml.homeCard;

import com.xunni.hotel.entity.HomeCard.DashboardStats;
import com.xunni.hotel.entity.HomeCard.OccupancyStats;
import com.xunni.hotel.entity.HomeCard.RegionDistribution;
import com.xunni.hotel.entity.HomeCard.Revenue;
import com.xunni.hotel.web.mapper.DashboardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private DashboardMapper dashboardMapper;

    public DashboardStats getDashboardStats() {
        LocalDate today = LocalDate.now();

        Integer totalRooms = dashboardMapper.countTotalRooms();
        Integer todayCheckIns = dashboardMapper.countTodayCheckIns(today);
        Integer todayCheckOuts = dashboardMapper.countTodayCheckOuts(today);
        Integer occupiedRooms = dashboardMapper.countOccupiedRooms(today);
        BigDecimal todayRevenue = dashboardMapper.sumTodayRevenue(today);

        // 空房数 = 总房量 - 在住房数
        Integer emptyRooms = 0;
        if (totalRooms != null && occupiedRooms != null) {
            emptyRooms = totalRooms - occupiedRooms;
        }

        // 入住率 = (在住房数 / 总房量) * 100
        BigDecimal occupancyRate = BigDecimal.ZERO;
        if (totalRooms != null && totalRooms > 0 && occupiedRooms != null) {
            occupancyRate = BigDecimal.valueOf(occupiedRooms)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalRooms), 1, RoundingMode.HALF_UP);
        }

        DashboardStats stats = new DashboardStats();
        stats.setTotalRooms(totalRooms);
        stats.setTodayCheckIns(todayCheckIns);
        stats.setTodayCheckOuts(todayCheckOuts);
        stats.setEmptyRooms(emptyRooms);
        stats.setOccupancyRate(occupancyRate);
        stats.setTodayRevenue(todayRevenue);

        return stats;
    }

    public OccupancyStats getOccupancyStats() {
        try {
            LocalDate today = LocalDate.now();
            
            OccupancyStats stats = new OccupancyStats();
            stats.setWeek(calculateOccupancyData(today, 7));
            stats.setMonth(calculateOccupancyData(today, 30));
            stats.setHalfYear(calculateOccupancyData(today, 180));
            
            return stats;
        } catch (Exception e) {
            System.err.println("Error calculating occupancy stats: " + e.getMessage());
            return new OccupancyStats();
        }
    }

    private List<OccupancyStats.OccupancyData> calculateOccupancyData(LocalDate today, int days) {
        try {
            List<OccupancyStats.OccupancyData> dataList = new ArrayList<>();
            Integer totalRooms = dashboardMapper.countTotalRooms();

            // Single batch query instead of N individual queries
            List<Map<String, Object>> batchResult = dashboardMapper.countOccupiedRoomsBatch(days - 1);

            // Build lookup map: date string -> count
            Map<String, Long> countByDate = new HashMap<>();
            for (Map<String, Object> row : batchResult) {
                Object dateObj = row.get("date");
                Object countObj = row.get("count");
                if (dateObj != null) {
                    String dateKey = dateObj.toString();
                    long count = countObj != null ? ((Number) countObj).longValue() : 0;
                    countByDate.put(dateKey, count);
                }
            }

            for (int i = days - 1; i >= 0; i--) {
                LocalDate date = today.minusDays(i);
                String dateKey = date.toString();
                double occupancyRate = 0.0;
                if (totalRooms != null && totalRooms > 0) {
                    Long occupiedRooms = countByDate.getOrDefault(dateKey, 0L);
                    occupancyRate = Math.round((double) occupiedRooms / totalRooms * 100 * 10) / 10.0;
                }
                OccupancyStats.OccupancyData data = new OccupancyStats.OccupancyData();
                data.setDate(dateKey);
                data.setOccupancyRate(occupancyRate);
                dataList.add(data);
            }

            return dataList;
        } catch (Exception e) {
            System.err.println("Error calculating occupancy data for " + days + " days: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<RegionDistribution> getRegionDistribution() {
        try {
            return dashboardMapper.getRegionDistribution();
        } catch (Exception e) {
            System.err.println("Error getting region distribution: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Revenue> getRevenue() {
        try {
            List<Map<String, Object>> rows = dashboardMapper.getRevenueRows();
            if (rows == null || rows.isEmpty()) {
                return new ArrayList<>();
            }
            List<String> dates = new ArrayList<>();
            List<BigDecimal> revenueList = new ArrayList<>();
            List<BigDecimal> revparList = new ArrayList<>();
            for (Map<String, Object> row : rows) {
                Object dateObj = row.get("date");
                dates.add(dateObj != null ? dateObj.toString() : "");
                Object revObj = row.get("revenue");
                revenueList.add(revObj != null ? new BigDecimal(revObj.toString()) : BigDecimal.ZERO);
                Object revparObj = row.get("revpar");
                revparList.add(revparObj != null ? new BigDecimal(revparObj.toString()) : BigDecimal.ZERO);
            }
            Revenue revenue = new Revenue();
            revenue.setDates(dates);
            revenue.setRevenue(revenueList);
            revenue.setRevpar(revparList);
            List<Revenue> result = new ArrayList<>();
            result.add(revenue);
            return result;
        } catch (Exception e) {
            System.err.println("Error getting revenue: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}