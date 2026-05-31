package com.xunni.hotel.web.service.iml;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunni.hotel.entity.Booking;
import com.xunni.hotel.web.dto.BookingDoto;
import com.xunni.hotel.web.mapper.BookingMapper;
import com.xunni.hotel.web.service.BookingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl extends ServiceImpl<BookingMapper, Booking> implements BookingService {

    @Override
    public List<Booking> selectUncheckedInOrders() {
        return baseMapper.selectUncheckedInOrders();
    }

    @Override
    public List<Booking> selectUncheckedInOrdersWithPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return baseMapper.selectUncheckedInOrdersWithPage(offset, pageSize);
    }

    @Override
    public int getUncheckedInOrdersTotal() {
        return baseMapper.getUncheckedInOrdersTotal();
    }

    @Override
    public Booking getOrderByPhone(String guestPhone) {
        return baseMapper.selectOrderByPhone(guestPhone);
    }

    @Override
    public int updateOrder(BookingDoto bookingDoto) {
        return baseMapper.updateOrder(bookingDoto);
    }

    @Override
    public boolean existsByOrderNo(String orderNo) {
        return baseMapper.countByOrderNo(orderNo) > 0;
    }

    @Override
    public int insertOrder(BookingDoto bookingDoto) {
        return baseMapper.insertOrder(bookingDoto);
    }
}
