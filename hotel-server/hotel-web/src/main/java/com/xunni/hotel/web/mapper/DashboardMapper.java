package com.xunni.hotel.web.mapper;

import com.xunni.hotel.entity.HomeCard.RegionDistribution;
import com.xunni.hotel.entity.HomeCard.Revenue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DashboardMapper {
        // 1. Total rooms
        @Select("SELECT COUNT(*) FROM room")
        Integer countTotalRooms();

        // 2. Today check-ins (check-in date = today and order status is 'checked_in')
        @Select("SELECT COUNT(*) FROM orders " +
                        "WHERE check_in_date = #{today} AND order_status = 'checked_in'")
        Integer countTodayCheckIns(@Param("today") LocalDate today);

        // 3. Today check-outs (check-out date = today and order status is 'checked_in'
        // or 'checked_out')
        // Usually today's check-out guests may still have 'checked_in' status, so just
        // match check-out date
        @Select("SELECT COUNT(*) FROM orders " +
                        "WHERE check_out_date = #{today} " +
                        "AND order_status IN ('checked_in', 'checked_out')")
        Integer countTodayCheckOuts(@Param("today") LocalDate today);

        // 4. Current occupied rooms (check-in date <= today < check-out date and order
        // status is 'checked_in')
        @Select("SELECT COUNT(DISTINCT room_id) FROM orders " +
                        "WHERE check_in_date <= #{today} AND check_out_date > #{today} " +
                        "AND order_status = 'checked_in'")
        Integer countOccupiedRooms(@Param("today") LocalDate today);

        // 5. Today revenue (sum of paid amount where check-out date = today and order
        // status is 'checked_out')
        @Select("SELECT COALESCE(SUM(paid_amount), 0) FROM orders " +
                        "WHERE check_out_date = #{today} AND order_status = 'checked_out'")
        BigDecimal sumTodayRevenue(@Param("today") LocalDate today);

        // 6. Region distribution statistics
        // @Select("SELECT " +
        // "region AS region, " +
        // "COUNT(*) AS orderCount, " +
        // "COALESCE(SUM(total_amount), 0) AS revenue, " +
        // "COALESCE(AVG(total_amount), 0) AS avgOrderAmount, " +
        // "ROUND(COUNT(*) * 100.0 / SUM(COUNT(*)) OVER(), 2) AS orderPercent, " +
        // "ROUND(COALESCE(SUM(total_amount), 0) * 100.0 /
        // COALESCE(SUM(SUM(total_amount)) OVER(), 1), 2) AS revenuePercent " +
        // "FROM orders " +
        // "WHERE region IS NOT NULL AND region != '' " +
        // "GROUP BY region " +
        // "ORDER BY orderCount DESC")

        @Select("SELECT " +
                        "region AS region, " +
                        "COUNT(*) AS orderCount, " +
                        "COALESCE(SUM(total_amount), 0) AS revenue, " +
                        "COALESCE(ROUND(AVG(total_amount), 2), 0) AS avgOrderAmount, " +
                        "ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM orders WHERE region IS NOT NULL AND region != ''), 2) AS orderPercent, "
                        +
                        "ROUND(COALESCE(SUM(total_amount), 0) * 100.0 / COALESCE((SELECT SUM(total_amount) FROM orders WHERE region IS NOT NULL AND region != ''), 1), 2) AS revenuePercent "
                        +
                        "FROM orders " +
                        "WHERE region IS NOT NULL AND region != '' " +
                        "GROUP BY region " +
                        "ORDER BY orderCount DESC")
        List<RegionDistribution> getRegionDistribution();

        // 7. Revenue statistics (last 30 days)
        @Select("WITH date_series AS (\n" +
                        "    SELECT DATE_SUB(CURDATE(), INTERVAL n DAY) AS date \n" +
                        "    FROM numbers \n" +
                        "    WHERE n BETWEEN 29 AND 0 \n" +
                        "),\n" +
                        "revenue_data AS (\n" +
                        "    SELECT \n" +
                        "        ds.date,\n" +
                        "        COALESCE(SUM(o.paid_amount), 0) AS revenue,\n" +
                        "        ROUND(COALESCE(SUM(o.paid_amount), 0) / (SELECT COUNT(*) FROM rooms), 2) AS revpar\n" +
                        "    FROM date_series ds\n" +
                        "    LEFT JOIN orders o ON o.check_out_date = ds.date AND o.order_status = 'checked_out'\n" +
                        "    GROUP BY ds.date\n" +
                        "    ORDER BY ds.date\n" +
                        ")\n" +
                        "SELECT \n" +
                        "    GROUP_CONCAT(date ORDER BY date) AS dates,\n" +
                        "    GROUP_CONCAT(revenue ORDER BY date) AS revenue,\n" +
                        "    GROUP_CONCAT(revpar ORDER BY date) AS revpar\n" +
                        "FROM revenue_data")
        List<Revenue> getRevenue();
}
