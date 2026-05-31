package com.xunni.hotel.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunni.hotel.entity.Room;
import com.xunni.hotel.web.dto.RoomDoto;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RoomMapper extends BaseMapper<RoomDoto> {
        List<RoomDoto> selectRoomList(@Param("query") RoomDoto query);

        @Select("SELECT r.room_id, r.room_number, b.building_name AS building, " +
                        "f.floor_name AS floor, rt.type_name AS room_type, rt.price AS price, rt.max_guests AS capacity, r.status, "
                        +
                        "rs.status_name, rs.status_color, " +
                        "CASE WHEN r.status IN ('occupied_clean', 'occupied_dirty', 'checked_in') THEN o.guest_name ELSE NULL END AS guest_name, "
                        +
                        "CASE WHEN r.status IN ('occupied_clean', 'occupied_dirty', 'checked_in') THEN o.check_in_date ELSE NULL END AS check_in_time, "
                        +
                        "CASE WHEN r.status IN ('occupied_clean', 'occupied_dirty', 'checked_in') THEN o.check_out_date ELSE NULL END AS check_out_time, "
                        +
                        "CASE WHEN r.status IN ('occupied_clean', 'occupied_dirty', 'checked_in') THEN COALESCE(o.adults + o.children, 0) ELSE 0 END AS current_guests "
                        +
                        "FROM room r " +
                        "LEFT JOIN building b ON r.building_id = b.building_id " +
                        "LEFT JOIN floor f ON r.floor_id = f.floor_id " +
                        "LEFT JOIN room_type rt ON r.type_id = rt.type_id " +
                        "LEFT JOIN room_status rs ON r.status = rs.status_code " +
                        "LEFT JOIN orders o ON r.room_id = o.room_id AND o.order_status IN ('checked_in', 'reserved') "
                        +
                        "ORDER BY b.building_id, f.floor_number, r.room_number")
        @Results(id = "RoomQueryVOMap", value = {
                        @Result(column = "room_id", property = "roomId"),
                        @Result(column = "room_number", property = "roomNumber"),
                        @Result(column = "building", property = "building"),
                        @Result(column = "floor", property = "floor"),
                        @Result(column = "room_type", property = "roomType"),
                        @Result(column = "price", property = "price"),
                        @Result(column = "capacity", property = "capacity"),
                        @Result(column = "status", property = "status"),
                        @Result(column = "status_name", property = "statusName"),
                        @Result(column = "status_color", property = "statusColor"),
                        @Result(column = "guest_name", property = "guestName"),
                        @Result(column = "check_in_time", property = "checkInTime"),
                        @Result(column = "check_out_time", property = "checkOutTime"),
                        @Result(column = "current_guests", property = "currentGuests"),
        })
        List<Room> selectAll();

        // -- 获取楼栋列表
        @Select("SELECT building_id AS value, building_name AS label FROM building ORDER BY building_id")
        List<Room.Building> selectBuildingList();

        // -- 获取楼层
        @Select("SELECT \n" +
                        "    MIN(f.floor_id) AS value,   -- 或 ANY_VALUE(f.floor_id)\n" +
                        "    f.floor_name AS label\n" +
                        "FROM floor f\n" +
                        "JOIN building b ON f.building_id = b.building_id\n" +
                        "GROUP BY f.floor_name\n" +
                        "ORDER BY f.floor_name;  ")
        List<Room.Building> selectFloorList();

        // -- 获取房型
        @Select("SELECT type_id AS value, type_name AS label FROM room_type ORDER BY type_id;")
        List<Room.Building> selectRoomTypeList();

        // 获取房间状态

        @Select("SELECT ANY_VALUE(status_code) AS status, status_name, ANY_VALUE(status_color) AS status_color\n" +
                        "FROM room_status\n" +
                        "GROUP BY status_name\n" +
                        "ORDER BY MIN(status_code)")
        List<Room.RoomStatus> selectRoomStatusList();

        @Select("SELECT MIN(type_code) AS value, type_name AS label \n" +
                        "FROM check_in_type \n" +
                        "GROUP BY type_name \n" +
                        "ORDER BY MIN(type_id)")
        List<Room.Building> selectCheckInTypeList();

        @Select("SELECT channel_code AS value, channel_name AS label FROM channel ORDER BY channel_id;")
        List<Room.Building> selectChannelList();

        // 未入住房间
        @Select("SELECT " +
                        "r.room_id AS roomId, " +
                        "r.room_number AS roomNumber, " + // 新增
                        "b.building_name AS building, " +
                        "f.floor_name AS floor, " +
                        "rt.type_name AS roomType, " +
                        "rt.price AS price, " +
                        "rt.max_guests AS capacity, " +
                        "rs.status_name AS statusName, " +
                        "rs.status_color AS statusColor " +
                        "FROM room r " +
                        "LEFT JOIN building b ON r.building_id = b.building_id " +
                        "LEFT JOIN floor f ON r.floor_id = f.floor_id " +
                        "LEFT JOIN room_type rt ON r.type_id = rt.type_id " +
                        "LEFT JOIN room_status rs ON r.status = rs.status_code " +
                        "WHERE r.status IN ('empty_clean', 'free') " +
                        "ORDER BY b.building_id, f.floor_number, r.room_number")
        List<RoomDoto> selectFreeRoom();

        // 已入住房间列表
        @Select("SELECT " +
                        "r.room_id AS roomId, " +
                        "r.room_number AS roomNumber, " +
                        "b.building_name AS building, " +
                        "f.floor_name AS floor, " +
                        "rt.type_name AS roomType, " +
                        "rt.price AS price, " +
                        "rt.max_guests AS capacity, " +
                        "rs.status_name AS statusName, " +
                        "rs.status_color AS statusColor, " +
                        "o.guest_name AS guestName, " +
                        "o.check_in_date AS checkInTime, " +
                        "o.check_out_date AS checkOutTime, " +
                        "COALESCE(o.adults + o.children, 0) AS currentGuests " +
                        "FROM room r " +
                        "LEFT JOIN building b ON r.building_id = b.building_id " +
                        "LEFT JOIN floor f ON r.floor_id = f.floor_id " +
                        "LEFT JOIN room_type rt ON r.type_id = rt.type_id " +
                        "LEFT JOIN room_status rs ON r.status = rs.status_code " +
                        "LEFT JOIN orders o ON r.room_id = o.room_id AND o.order_status = 'checked_in' " +
                        "WHERE r.status IN ('occupied_clean', 'occupied_dirty', 'checked_in') " +
                        "ORDER BY b.building_id, f.floor_number, r.room_number")
        List<RoomDoto> selectCheckedInRoom();

        // 更新入住信息（更新orders表中的客人信息）
        @Update("UPDATE orders SET guest_name = #{guestName} WHERE room_id = #{roomId} AND order_status = 'checked_in'")
        int updateCheckin(RoomDoto roomDoto);

        @Update("UPDATE orders SET guest_name = #{guestName} WHERE room_id = #{roomId} AND order_status = 'checked_in'")
        int updateCheckinByParams(@Param("roomId") Integer roomId, @Param("guestName") String guestName);

        // 退房：更新订单状态和房间状态
        @Update("<script>" +
                        "UPDATE orders SET " +
                        "order_status = 'checked_out'" +
                        "<if test='remark != null'>, remarks = #{remark}</if>" +
                        " WHERE order_id = #{orderId}" +
                        "</script>")
        int checkoutRoom(@Param("roomId") Integer roomId, @Param("orderId") Integer orderId, 
                        @Param("checkOutTime") String checkOutTime, @Param("remark") String remark);

        // 更新房间状态为空房
        @Update("UPDATE room SET status = 'empty_clean' WHERE room_id = #{roomId}")
        int updateRoomStatusToClean(@Param("roomId") Integer roomId);

        // 更新房间状态为已派单
        @Update("UPDATE room SET status = 'pending_cleaning' WHERE room_id = #{roomId}")
        int updateRoomStatusToPendingCleaning(@Param("roomId") Integer roomId);

        // 更新房间状态为脏房
        @Update("UPDATE room SET status = 'dirty' WHERE room_id = #{roomId}")
        int updateRoomStatusToDirty(@Param("roomId") Integer roomId);

        // 根据房间ID查询房间号
        @Select("SELECT room_number FROM room WHERE room_id = #{roomId}")
        String selectRoomNumberById(@Param("roomId") Integer roomId);
}
