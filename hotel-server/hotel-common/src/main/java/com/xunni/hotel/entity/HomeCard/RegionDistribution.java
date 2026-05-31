package com.xunni.hotel.entity.HomeCard;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RegionDistribution {
    private String region;
    private Integer orderCount;
    private BigDecimal revenue;
    private BigDecimal avgOrderAmount;
    private BigDecimal orderPercent;
    private BigDecimal revenuePercent;
}
