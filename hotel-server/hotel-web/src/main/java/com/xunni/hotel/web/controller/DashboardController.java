package com.xunni.hotel.web.controller;

import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.entity.HomeCard.DashboardStats;
import com.xunni.hotel.entity.HomeCard.OccupancyStats;
import com.xunni.hotel.entity.HomeCard.RegionDistribution;
import com.xunni.hotel.entity.HomeCard.Revenue;
import com.xunni.hotel.web.service.iml.homeCard.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public Result<DashboardStats> getStats() {
        DashboardStats stats = dashboardService.getDashboardStats();
        return Result.success(stats);
    }

    @GetMapping("/occupancy")
    public Result<OccupancyStats> getOccupancyStats() {
        OccupancyStats stats = dashboardService.getOccupancyStats();
        return Result.success(stats);
    }

    @GetMapping("/region-distribution")
    public Result<List<RegionDistribution>> getRegionDistribution() {
        List<RegionDistribution> distribution = dashboardService.getRegionDistribution();
        return Result.success(distribution);
    }

    @GetMapping("/revenue")
    public Result<List<Revenue>> getRevenue() {
        List<Revenue> revenue = dashboardService.getRevenue();
        return Result.success(revenue);
    }
}