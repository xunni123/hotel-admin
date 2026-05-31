package com.xunni.hotel.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunni.hotel.entity.Booking;
import com.xunni.hotel.web.dto.BookingDoto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookingMapper extends BaseMapper<Booking> {

    // 查询所有未入住订单
    @Select("SELECT * FROM orders WHERE order_status IN ('pending', 'confirmed', 'reserved')")
    @Results(id = "BookingResultMap", value = {
            @Result(column = "order_id", property = "orderId"),
            @Result(column = "order_no", property = "orderNo"),
            @Result(column = "room_number", property = "roomNumber"),
            @Result(column = "guest_name", property = "guestName"),
            @Result(column = "guest_phone", property = "guestPhone"),
            @Result(column = "check_in_date", property = "checkInDate"),
            @Result(column = "check_out_date", property = "checkOutDate"),
            @Result(column = "total_amount", property = "totalAmount"),
            @Result(column = "order_status", property = "orderStatus"),
            @Result(column = "payment_status", property = "paymentStatus"),
            @Result(column = "book_channel", property = "bookChannel")
    })
    List<Booking> selectUncheckedInOrders();

    // 分页查询未入住订单
    @Select("SELECT * FROM orders WHERE order_status IN ('pending', 'confirmed', 'reserved') ORDER BY order_id DESC LIMIT #{offset}, #{pageSize}")
    @ResultMap("BookingResultMap")
    List<Booking> selectUncheckedInOrdersWithPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 统计未入住订单总数
    @Select("SELECT COUNT(*) FROM orders WHERE order_status IN ('pending', 'confirmed', 'reserved')")
    int getUncheckedInOrdersTotal();

    // 根据客人电话查询订单（模糊查询）
    @Select("SELECT * FROM orders WHERE guest_phone LIKE CONCAT('%', #{guestPhone}, '%') LIMIT 1")
    @ResultMap("BookingResultMap")
    Booking selectOrderByPhone(@Param("guestPhone") String guestPhone);

    // 更新订单
    @Update("UPDATE orders SET " +
            "guest_name = #{booking.guestName}, " +
            "guest_phone = #{booking.guestPhone}, " +
            "check_in_date = #{booking.checkInDate}, " +
            "check_out_date = #{booking.checkOutDate}, " +
            "nights = #{booking.nights}, " +
            "total_amount = #{booking.totalAmount}, " +
            "order_status = #{booking.orderStatus}, " +
            "payment_status = #{booking.paymentStatus}, " +
            "book_channel = #{booking.bookChannel}, " +
            "check_in_type = #{booking.checkInType}, " +
            "remarks = #{booking.remarks} " +
            "WHERE order_id = #{booking.orderId}")
    int updateOrder(@Param("booking") BookingDoto bookingDoto);

    // 查询存在订单
    @Select("SELECT COUNT(*) FROM orders WHERE order_no = #{orderNo}")
    int countByOrderNo(@Param("orderNo") String orderNo);

    // 新增订单
    @Insert("INSERT INTO orders (" +
            "order_no, room_id, room_number, guest_name, guest_phone, id_card, " +
            "check_in_date, check_out_date, nights, adults, children, " +
            "total_amount, paid_amount, order_status, payment_status, " +
            "payment_method, book_channel, check_in_type, customer_type, remarks) " +
            "VALUES (" +
            "#{booking.orderNo}, #{booking.roomId}, #{booking.roomNumber}, " +
            "#{booking.guestName}, #{booking.guestPhone}, #{booking.idCard}, " +
            "#{booking.checkInDate}, #{booking.checkOutDate}, #{booking.nights}, " +
            "#{booking.adults}, #{booking.children}, #{booking.totalAmount}, " +
            "#{booking.paidAmount}, #{booking.orderStatus}, #{booking.paymentStatus}, " +
            "#{booking.paymentMethod}, #{booking.bookChannel}, #{booking.checkInType}, " +
            "#{booking.customerType}, #{booking.remarks})")
    @Options(useGeneratedKeys = true, keyProperty = "booking.orderId")
    int insertOrder(@Param("booking") BookingDoto bookingDoto);
}
