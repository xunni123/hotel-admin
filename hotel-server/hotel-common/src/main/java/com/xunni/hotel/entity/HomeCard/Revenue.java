package com.xunni.hotel.entity.HomeCard;

import lombok.Data;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.List;

@Data
public class Revenue {
    private List<String> dates;
    private List<BigDecimal> revenue;
    private List<BigDecimal> revpar;
}
