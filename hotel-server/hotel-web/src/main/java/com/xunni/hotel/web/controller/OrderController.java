package com.xunni.hotel.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.entity.Order;
import com.xunni.hotel.web.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public Result getOrderList(@RequestParam(required = false) String keyword,
                                @RequestParam(required = false) String status,
                                @RequestParam(required = false) String startDate,
                                @RequestParam(required = false) String endDate) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.and(w -> w
                    .like(Order::getOrderNo, keyword)
                    .or()
                    .like(Order::getRoomNumber, keyword)
                    .or()
                    .like(Order::getGuestName, keyword));
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq(Order::getOrderStatus, status);
        }
        if (StringUtils.hasText(startDate)) {
            queryWrapper.ge(Order::getCheckInDate, startDate);
        }
        if (StringUtils.hasText(endDate)) {
            queryWrapper.le(Order::getCheckInDate, endDate);
        }
        queryWrapper.orderByDesc(Order::getCreateTime);
        List<Order> orders = orderService.list(queryWrapper);
        return Result.success(orders);
    }

    @PostMapping("/add")
    public Result addOrder(@RequestBody Order order) {
        order.setCreateTime(LocalDateTime.now());
        boolean success = orderService.save(order);
        return success ? Result.success(order) : Result.error("添加订单失败");
    }

    @PutMapping("/update/{orderId}")
    public Result updateOrder(@PathVariable Integer orderId, @RequestBody Order order) {
        order.setOrderId(orderId);
        boolean success = orderService.updateById(order);
        return success ? Result.success(order) : Result.error("更新订单失败");
    }

    @DeleteMapping("/{orderId}")
    public Result deleteOrder(@PathVariable Integer orderId) {
        boolean success = orderService.removeById(orderId);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
}