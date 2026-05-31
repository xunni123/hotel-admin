package com.xunni.hotel.entity.HomeCard;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardStats {
    private Integer totalRooms;       // 总房量
    private Integer todayCheckIns;    // 今日入住数
    private Integer todayCheckOuts;   // 今日退房数
    private Integer emptyRooms;       // 空房数
    private BigDecimal occupancyRate; // 入住率（%）
    private BigDecimal todayRevenue;  // 今日营收
}
