package com.xunni.hotel.web.controller;

import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.entity.Booking;
import com.xunni.hotel.web.dto.BookingDoto;
import com.xunni.hotel.web.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/list")
    public Result<List<Booking>> getBookingList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        List<Booking> bookingList = bookingService.selectUncheckedInOrdersWithPage(page, pageSize);
        int total = bookingService.getUncheckedInOrdersTotal();
        return Result.success(bookingList, total);
    }

    @GetMapping("/phone/{guestPhone}")
    public Result<Booking> getOrderByPhone(@PathVariable("guestPhone") String guestPhone) {
        Booking booking = bookingService.getOrderByPhone(guestPhone);
        if (booking != null) {
            return Result.success(booking);
        } else {
            return Result.error("订单不存在");
        }
    }

    @PutMapping("/update")
    public Result<?> updateOrder(@Validated @RequestBody BookingDoto bookingDoto) {
        int result = bookingService.updateOrder(bookingDoto);
        if (result > 0) {
            return Result.success("更新订单成功");
        } else {
            return Result.error("更新订单失败");
        }
    }

    @PostMapping("/add")
    public Result<?> addOrder(@Validated @RequestBody BookingDoto bookingDoto) {
        if (bookingService.existsByOrderNo(bookingDoto.getOrderNo())) {
            return Result.error("订单已存在");
        }

        int result = bookingService.insertOrder(bookingDoto);
        if (result > 0) {
            return Result.success("新增订单成功", bookingDoto.getOrderId());
        } else {
            return Result.error("新增订单失败");
        }
    }
}
