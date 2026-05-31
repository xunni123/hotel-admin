package com.xunni.hotel.entity.HomeCard;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;



@Data
public class RevenueStats {
    private List<String> dates;
    private List<BigDecimal> revenue;
    private List<BigDecimal> revpar;
}
