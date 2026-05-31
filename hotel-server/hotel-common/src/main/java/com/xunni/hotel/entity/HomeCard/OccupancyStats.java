package com.xunni.hotel.entity.HomeCard;

import lombok.Data;
import java.util.List;

@Data
public class OccupancyStats {
    private List<OccupancyData> week;
    private List<OccupancyData> month;
    private List<OccupancyData> halfYear;

    @Data
    public static class OccupancyData {
        private String date;
        private double occupancyRate;
    }
}
