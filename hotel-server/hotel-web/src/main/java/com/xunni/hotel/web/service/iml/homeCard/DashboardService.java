package com.xunni.hotel.web.service.iml.homeCard;

import com.xunni.hotel.entity.HomeCard.DashboardStats;
import com.xunni.hotel.entity.HomeCard.OccupancyStats;
import com.xunni.hotel.entity.HomeCard.RegionDistribution;
import com.xunni.hotel.entity.HomeCard.Revenue;
import com.xunni.hotel.web.mapper.DashboardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Cacheable(value = "occupancyStats", key = "#root.methodName")
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
            
            for (int i = days - 1; i >= 0; i--) {
                LocalDate date = today.minusDays(i);
                Integer occupiedRooms = dashboardMapper.countOccupiedRooms(date);
                double occupancyRate = 0.0;
                if (totalRooms != null && totalRooms > 0 && occupiedRooms != null) {
                    occupancyRate = Math.round((double) occupiedRooms / totalRooms * 100 * 10) / 10.0;
                }
                OccupancyStats.OccupancyData data = new OccupancyStats.OccupancyData();
                data.setDate(date.toString());
                data.setOccupancyRate(occupancyRate);
                dataList.add(data);
            }
            
            return dataList;
        } catch (Exception e) {
            System.err.println("Error calculating occupancy data for " + days + " days: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Cacheable(value = "regionDistribution", key = "#root.methodName")
    public List<RegionDistribution> getRegionDistribution() {
        try {
            return dashboardMapper.getRegionDistribution();
        } catch (Exception e) {
            System.err.println("Error getting region distribution: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Cacheable(value = "revenue", key = "#root.methodName")
    public List<Revenue> getRevenue() {
        try {
            // 手动创建Revenue对象并填充数据
            List<Revenue> revenueList = new ArrayList<>();
            
            // 创建第一个Revenue对象（前10天）
            Revenue revenue1 = new Revenue();
            revenue1.setDates(Arrays.asList("2026-04-01", "2026-04-02", "2026-04-03", "2026-04-04", "2026-04-05", "2026-04-06", "2026-04-07", "2026-04-08", "2026-04-09", "2026-04-10"));
            revenue1.setRevenue(Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO, new BigDecimal(1200), BigDecimal.ZERO, new BigDecimal(1800), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            revenue1.setRevpar(Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO, new BigDecimal(120), BigDecimal.ZERO, new BigDecimal(180), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            revenueList.add(revenue1);
            
            // 创建第二个Revenue对象（中间10天）
            Revenue revenue2 = new Revenue();
            revenue2.setDates(Arrays.asList("2026-04-11", "2026-04-12", "2026-04-13", "2026-04-14", "2026-04-15", "2026-04-16", "2026-04-17", "2026-04-18", "2026-04-19", "2026-04-20"));
            revenue2.setRevenue(Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            revenue2.setRevpar(Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            revenueList.add(revenue2);
            
            // 创建第三个Revenue对象（后10天）
            Revenue revenue3 = new Revenue();
            revenue3.setDates(Arrays.asList("2026-04-21", "2026-04-22", "2026-04-23", "2026-04-24", "2026-04-25", "2026-04-26", "2026-04-27", "2026-04-28", "2026-04-29", "2026-04-30"));
            revenue3.setRevenue(Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            revenue3.setRevpar(Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            revenueList.add(revenue3);
            
            return revenueList;
        } catch (Exception e) {
            System.err.println("Error getting revenue: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}