package com.xunni.hotel.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xunni.hotel.entity.Booking;
import com.xunni.hotel.web.dto.BookingDoto;

import java.util.List;

public interface BookingService extends IService<Booking> {
    List<Booking> selectUncheckedInOrders();

    List<Booking> selectUncheckedInOrdersWithPage(int page, int pageSize);

    int getUncheckedInOrdersTotal();

    Booking getOrderByPhone(String guestPhone);

    int updateOrder(BookingDoto bookingDoto);

    boolean existsByOrderNo(String orderNo);

    int insertOrder(BookingDoto bookingDoto);

}
